package com.backend.backend.repositories.brand;

import com.backend.backend.entities.brand.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Integer> {
}
