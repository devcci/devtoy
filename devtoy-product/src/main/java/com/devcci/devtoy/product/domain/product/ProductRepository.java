package com.devcci.devtoy.product.domain.product;

import com.devcci.devtoy.product.infra.persistence.ProductQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueryRepository {

    List<Product> findAllByBrandId(Long brandId);

}
