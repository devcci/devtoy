package com.devcci.devtoy.product.domain.brand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    Optional<Brand> findByName(String brandName);

    boolean existsByName(String brandName);
}
