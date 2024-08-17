package com.devcci.devtoy.product.domain.product;

import com.devcci.devtoy.common.domain.BaseTimeEntity;
import com.devcci.devtoy.product.common.util.BigDecimalUtil;
import com.devcci.devtoy.product.domain.brand.Brand;
import com.devcci.devtoy.product.domain.category.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.Objects;

@DynamicUpdate
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product",
    uniqueConstraints = {@UniqueConstraint
        (name = "product_brand_category_uq", columnNames = {"name", "brand_id", "category_id"})
    },
    indexes = @Index(
        name = "category_price_idx", columnList = "category_id, price"
    )
)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price", nullable = false, precision = 15, scale = 0)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", foreignKey = @ForeignKey(name = "product_brand_fk"))
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "product_category_fk"))
    private Category category;

    @Comment("상품의 간략한 설명")
    @Column(name = "description")
    private String description;

    @Builder
    private Product(String name, BigDecimal price, Brand brand, Category category, String description) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.category = category;
        this.description = description;
    }

    public static Product createProduct(String name, BigDecimal price, Brand brand, Category category, String description) {
        return Product.builder()
            .name(name)
            .description(description)
            .price(price)
            .brand(brand)
            .category(category)
            .build();
    }

    public void update(Product product) {
        this.name = product.getName();
        this.brand = product.getBrand();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.description = product.getDescription();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(name, product.name) &&
            BigDecimalUtil.equals(price, product.price) &&
            Objects.equals(brand, product.brand) &&
            Objects.equals(category, product.category) &&
            Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, brand, category, description);
    }
}