package com.backend.backend.controllers.product;

import com.backend.backend.dtos.product.ProductResponse;
import com.backend.backend.entities.product.Product;
import com.backend.backend.services.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
//@CrossOrigin(origins = "https://shopping-center-lime.vercel.app")
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;
    @GetMapping("/all")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer id){
        System.out.print(id);
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product, @RequestParam("file") MultipartFile file){
        Product pro = productService.createProduct(product,file);
        return ResponseEntity.ok(pro);
    }
}
