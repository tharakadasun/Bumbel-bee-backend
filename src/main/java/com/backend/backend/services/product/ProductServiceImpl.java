package com.backend.backend.services.product;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.mq.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.backend.backend.dtos.product.ProductRequest;
import com.backend.backend.dtos.product.ProductResponse;
import com.backend.backend.entities.brand.Brand;
import com.backend.backend.entities.category.Category;
import com.backend.backend.entities.product.Product;
import com.backend.backend.entities.user.User;
import com.backend.backend.repositories.brand.BrandRepository;
import com.backend.backend.repositories.category.CategoryRepository;
import com.backend.backend.repositories.product.ProductRepository;
import com.backend.backend.repositories.user.UserRepository;
import com.backend.backend.services.file.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Value("${bucketName}")
    private String bucketName;

    @Value("${accessKey}")
    private String accessKey;

    @Value("${secret}")
    private String secret;
    @Value("${region}")
    private String region;


    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponse = products.stream().map(product -> new ProductResponse(
                product.getId(),
                product.getProductName(),
                product.getPerUnitPrice(),
                product.getDescription(),
                generateImageURL(product.getImageName()),
                product.getCategory(),
                product.getBrand()
        )).collect(Collectors.toList());
        return productResponse;
    }

    @Override
    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);
        productResponse.setImageURL(generateImageURL(product.getImageName()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        User currentUser = userRepository.findByEmail(currentUserName)
                .orElse(null);

        if (currentUser != null) {
            double loanBalance = currentUser.getLoan().getLoanBalance();
            productResponse.setIsLoanActive(product.getPerUnitPrice() < loanBalance);
        }
        return productResponse;
    }

    @Override
    public Product createProduct(String productName,Double perUnitPrice,String description,Integer categoryId,Integer brandId, MultipartFile  file) {
        String fileName = file.getOriginalFilename();
        System.out.println("fileName"+fileName);
        Product newProduct = new Product();
        Category category = categoryRepository.findById(categoryId).get();
        Brand brand = brandRepository.findById(brandId).get();
        System.out.println("category"+category);
        System.out.println("brand"+brand);
        newProduct.setProductName(productName);
        newProduct.setBrand(brand);
        newProduct.setCategory(category);
        newProduct.setDescription(description);
        newProduct.setPerUnitPrice(perUnitPrice);
        newProduct.setImageName(fileName);
        s3Service.saveFile(file);
        productRepository.save(newProduct);
        return newProduct;
    }

    public String generateImageURL(String imageName){
        if(!imageName.equals(null)){
            BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secret);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(region)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .build();
            Date expiration = new Date(System.currentTimeMillis() + 3600000);
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, imageName)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(expiration);
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toString();
        }else{
            return null;
        }
    }

}
