package com.backend.backend.dtos.product;

import com.backend.backend.entities.brand.Brand;
import com.backend.backend.entities.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Integer id;
    private String productName;
    private Double perUnitPrice;
    private String description;
    private Boolean isLoanActive;
    private String imageURL;
    private Category category;
    private Brand brand;

    public ProductResponse(Integer id, String productName, Double perUnitPrice, String description, String imageURL, Category category, Brand brand) {
        this.id = id;
        this.productName = productName;
        this.perUnitPrice = perUnitPrice;
        this.description = description;
        this.imageURL = imageURL;
        this.category = category;
        this.brand = brand;
    }
}
