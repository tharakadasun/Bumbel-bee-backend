package com.backend.backend.services.product;

import com.backend.backend.dtos.product.ProductResponse;
import com.backend.backend.entities.product.Product;
import com.backend.backend.entities.user.User;
import com.backend.backend.repositories.product.ProductRepository;
import com.backend.backend.repositories.user.UserRepository;
import com.backend.backend.services.file.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    @Override
    public ProductResponse getProductById(Integer id) {
        ProductResponse productResponse = new ProductResponse();
        Product product = productRepository.findById(id).get();
        BeanUtils.copyProperties(product,productResponse);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserName).get();

        if(currentUser != null){
            double loanBalance = currentUser.getLoan().getLoanBalance();
            if(product.getPerUnitPrice()<loanBalance){
                productResponse.setIsLoanActive(true);
            }else{
                productResponse.setIsLoanActive(false);
            }
        }

        return productResponse;
    }

    @Override
    public Product createProduct(Product product, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Product newProduct = new Product();
        BeanUtils.copyProperties(product,newProduct);
        newProduct.setImageName(fileName);
        s3Service.saveFile(file);
        productRepository.save(newProduct);
        return newProduct;
    }
}
