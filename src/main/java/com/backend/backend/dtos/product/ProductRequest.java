package com.backend.backend.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private Integer id;
    private String productName;
    private Double perUnitPrice;
    private String description;
    private Integer categoryId;
    private Integer brandId;

}
