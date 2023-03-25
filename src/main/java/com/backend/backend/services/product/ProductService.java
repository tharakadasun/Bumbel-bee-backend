package com.backend.backend.services.product;

import com.backend.backend.dtos.product.ProductRequest;
import com.backend.backend.dtos.product.ProductResponse;
import com.backend.backend.entities.product.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Integer id);

    Product createProduct(String productName,Double perUnitPrice,String description,Integer categoryId,Integer brandId, MultipartFile file);
}
