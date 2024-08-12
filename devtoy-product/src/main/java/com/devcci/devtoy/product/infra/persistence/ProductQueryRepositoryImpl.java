package com.devcci.devtoy.product.infra.persistence;

import com.devcci.devtoy.product.domain.product.Product;
import com.devcci.devtoy.product.infra.persistence.projection.LowestProductByBrandProjection;
import com.devcci.devtoy.product.infra.persistence.projection.LowestProductByCategoryProjection;
import com.devcci.devtoy.product.infra.persistence.projection.PriceByCategoryProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class ProductQueryRepositoryImpl implements ProductQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String PARAMETER_CATEGORY_NAME = "categoryName";
    private static final String PARAMETER_BRAND_NAME = "brandName";
    private static final String PARAMETER_PRODUCT_NAME = "productName";
    private static final String PARAMETER_PRODUCT_ID = "productId";

    @Override
    public List<Product> findAllFetchJoin(Pageable pageable) {
        String jpql =
            "SELECT p FROM Product p JOIN FETCH p.brand b JOIN FETCH p.category c";
        if (pageable.getSort().isSorted()) {
            StringJoiner orderByClause = new StringJoiner(", ", " ORDER BY ", "");
            for (Sort.Order order : pageable.getSort()) {
                orderByClause.add("p." + order.getProperty() + " " + order.getDirection().name());
            }
            jpql += orderByClause.toString();
        }
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    @Override
    public Optional<Product> findByIdFetchJoin(Long productId) {
        String jpql = "SELECT p FROM Product p JOIN FETCH p.brand b JOIN FETCH p.category c WHERE p.id = :productId";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
        query.setParameter(PARAMETER_PRODUCT_ID, productId);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (EmptyResultDataAccessException | NoResultException e) {
            return Optional.empty();
        }
    }


    @Override
    public boolean existsByNameAndBrandNameAndCategoryName(String productName, String brandName, String categoryName) {
        String jpql = "SELECT COUNT(p.id) > 0 FROM Product p JOIN p.brand b JOIN p.category c WHERE p.name = :productName AND b.name = :brandName AND c.name = :categoryName";
        TypedQuery<Boolean> query = entityManager.createQuery(jpql, Boolean.class);
        query.setParameter(PARAMETER_PRODUCT_NAME, productName);
        query.setParameter(PARAMETER_BRAND_NAME, brandName);
        query.setParameter(PARAMETER_CATEGORY_NAME, categoryName);
        return query.getSingleResult();
    }

    @Override
    public List<LowestProductByCategoryProjection> findLowestPriceProductByCategory() {
        String jpql =
            "SELECT new com.devcci.devtoy.product.infra.persistence.projection " +
                ".LowestProductByCategoryProjection(c.name, b.name, p.price) " +
                "FROM Product p " +
                "JOIN Brand b ON p.brand.id = b.id " +
                "JOIN Category c ON p.category.id = c.id " +
                "WHERE p.id IN (SELECT MAX(p.id) " +
                "FROM Product p " +
                "WHERE (p.category.id, p.price) IN ( " +
                "SELECT p.category.id, MIN(p.price) " +
                "FROM Product p " +
                "GROUP BY p.category.id) " +
                "GROUP BY p.category.id, p.price) " +
                "ORDER BY c.name";
        return entityManager.createQuery(jpql,
            LowestProductByCategoryProjection.class).getResultList();
    }

    @Override
    public List<LowestProductByBrandProjection> findLowestPriceProductByBrand() {
        String jpql =
            "SELECT new com.devcci.devtoy.product.infra.persistence.projection.LowestProductByBrandProjection(b.name, c.name, MIN(p.price)) "
                +
                "FROM Product p " +
                "JOIN Brand b ON p.brand.id = b.id " +
                "JOIN Category c ON p.category.id = c.id " +
                "GROUP BY b.id, c.id";
        TypedQuery<LowestProductByBrandProjection> query = entityManager.createQuery(jpql,
            LowestProductByBrandProjection.class);
        return query.getResultList();
    }

    @Override
    public Optional<PriceByCategoryProjection> findLowestPriceByCategory(String categoryName) {
        String jpql = "SELECT b.name as brandName, MIN(p.price) as productPrice " +
            "FROM Product p " +
            "JOIN Brand b ON p.brand.id = b.id " +
            "JOIN Category c ON c.id = p.category.id " +
            "WHERE c.name = :categoryName " +
            "GROUP BY b.name " +
            "ORDER BY productPrice " +
            "LIMIT 1";
        TypedQuery<PriceByCategoryProjection> query = entityManager.createQuery(jpql,
            PriceByCategoryProjection.class);
        query.setParameter(PARAMETER_CATEGORY_NAME, categoryName);
        try {
            return Optional.of(query.getSingleResult());
        } catch (EmptyResultDataAccessException | NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<PriceByCategoryProjection> findHighestPriceByCategory(String categoryName) {
        String jpql = "SELECT b.name as brandName, MAX(p.price) as productPrice " +
            "FROM Product p " +
            "JOIN Brand b ON b.id = p.brand.id " +
            "JOIN Category c ON c.id = p.category.id " +
            "WHERE c.name = :categoryName " +
            "GROUP BY b.name " +
            "ORDER BY productPrice DESC " +
            "LIMIT 1";
        TypedQuery<PriceByCategoryProjection> query = entityManager.createQuery(jpql,
            PriceByCategoryProjection.class);
        query.setParameter(PARAMETER_CATEGORY_NAME, categoryName);
        try {
            return Optional.of(query.getSingleResult());
        } catch (EmptyResultDataAccessException | NoResultException e) {
            return Optional.empty();
        }
    }
}