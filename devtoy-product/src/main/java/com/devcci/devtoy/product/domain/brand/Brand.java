package com.devcci.devtoy.product.domain.brand;

import com.devcci.devtoy.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "brand",
    uniqueConstraints = {
        @UniqueConstraint(name = "brand_name_uq", columnNames = "name")
    })
public class Brand extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    private Brand(String name) {
        this.name = name;
    }

    public static Brand createBrand(String name) {
        return new Brand(name);
    }

    public void updateBrandName(String newName) {
        this.name = newName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Brand brand)) return false;
        return Objects.equals(name, brand.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
