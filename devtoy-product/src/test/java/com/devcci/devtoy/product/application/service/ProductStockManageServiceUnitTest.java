package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.common.infra.kafka.dto.OrderMessage;
import com.devcci.devtoy.common.infra.kafka.dto.OrderMessage.OrderProductMessage;
import com.devcci.devtoy.product.config.UnitTest;
import com.devcci.devtoy.product.domain.brand.Brand;
import com.devcci.devtoy.product.domain.category.Category;
import com.devcci.devtoy.product.domain.product.Product;
import com.devcci.devtoy.product.domain.product.ProductRepository;
import com.devcci.devtoy.product.infra.kafka.OrderResultMessageProducer;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@UnitTest
class ProductStockManageServiceUnitTest {
    @InjectMocks
    private ProductStockManageService productStockManageService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderResultMessageProducer orderResultMessageProducer;

    @Test
    void modifyStockQuantity() {
        // given
        Long productId = 1L;
        Long quantity = 50L;
        Product product = Product.createProduct("product1", new BigDecimal("1500"), mock(Brand.class), mock(Category.class), "description", 100L);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        productStockManageService.modifyStockQuantity(productId, quantity);

        // then
        assertThat(product.getStockQuantity()).isEqualTo(50L);
    }

    @Test
    void removeStockQuantity() {
        // given
        OrderMessage orderMessage =
            OrderMessage.of(
                1L, "tester1",
                List.of(
                    OrderProductMessage.of(1L, 50L, new BigDecimal("1500")),
                    OrderProductMessage.of(2L, 100L, new BigDecimal("2000")))
            );
        Product product1 = Product.createProduct("product1", new BigDecimal("1500"), mock(Brand.class), mock(Category.class), "description", 100L);
        Product product2 = Product.createProduct("product2", new BigDecimal("2000"), mock(Brand.class), mock(Category.class), "description", 200L);
        given(productRepository.findByIdWithPessimisticLock(1L)).willReturn(Optional.of(product1));
        given(productRepository.findByIdWithPessimisticLock(2L)).willReturn(Optional.of(product2));

        // when
        productStockManageService.removeStockQuantity(orderMessage);

        // then
        assertThat(product1.getStockQuantity()).isEqualTo(50L);
        assertThat(product2.getStockQuantity()).isEqualTo(100L);

    }
}