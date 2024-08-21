package com.devcci.devtoy.product.web.api;

import com.devcci.devtoy.product.application.dto.CategoryPriceRangeResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceBrandProductsResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceBrandProductsResponse.LowestPriceBrandProduct.BrandProduct;
import com.devcci.devtoy.product.application.dto.LowestPriceCategoryResponse;
import com.devcci.devtoy.product.application.dto.ProductResponse;
import com.devcci.devtoy.product.config.IntegrationTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ProductSearchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("상품 조회")
    @Test
    void getAllProducts() throws Exception {
        // given
        String page = "1";
        String size = "10";
        String sort = "asc";

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/product")
                .param("page", page)
                .param("size", size)
                .param("sort", sort)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        // then
        String content = result.getResponse().getContentAsString();
        assertThat(content).isNotBlank();
        List<ProductResponse> productList = objectMapper.readValue(content,
            new TypeReference<>() {
            });
        assertThat(productList).isNotEmpty().hasSize(10);
    }

    @DisplayName("카테고리별 최저가 상품 조회")
    @Test
    void getLowestPriceCategory() throws Exception {
        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/product/categories/lowest")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        // then
        String content = result.getResponse().getContentAsString();
        assertThat(content).isNotBlank();
        LowestPriceCategoryResponse response = objectMapper.readValue(content,
            LowestPriceCategoryResponse.class);
        assertThat(response.getTotalPrice()).isEqualTo("34,100");
        List<LowestPriceCategoryResponse.CategoryProduct> products = response.getCategoryProducts();
        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getCategoryName()).isEqualTo("가방");
        assertThat(products.get(0).getBrandName()).isEqualTo("A");
        assertThat(products.get(0).getPrice()).isEqualTo("2,000");
        assertThat(products.get(1).getCategoryName()).isEqualTo("모자");
        assertThat(products.get(1).getBrandName()).isEqualTo("D");
        assertThat(products.get(1).getPrice()).isEqualTo("1,500");
    }

    @DisplayName("최저가 브랜드 상품 조회")
    @Test
    void getLowestPriceBrand() throws Exception {
        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/product/brands/lowest")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        // then
        String content = result.getResponse().getContentAsString();
        assertThat(content).isNotBlank();
        LowestPriceBrandProductsResponse response = objectMapper.readValue(content,
            LowestPriceBrandProductsResponse.class);

        assertThat(response.getLowestPriceBrandProduct().getBrandName()).isEqualTo("D");
        assertThat(response.getLowestPriceBrandProduct().getTotalPrice()).isEqualTo("36,100");
        List<BrandProduct> products =
            response.getLowestPriceBrandProduct()
                .getBrandProducts();
        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getCategoryName()).isEqualTo("상의");
        assertThat(products.get(0).getPrice()).isEqualTo("10,100");
        assertThat(products.get(1).getCategoryName()).isEqualTo("아우터");
        assertThat(products.get(1).getPrice()).isEqualTo("5,100");
    }

    @DisplayName("카테고리의 최저가, 최고가 상품 조회")
    @Test
    void getCategoryMinMaxPrices() throws Exception {
        // given
        String categoryName = "상의";

        // when
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/product/categories/min-max-prices")
                    .param("categoryName", categoryName)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())

            .andReturn();

        // then
        String content = result.getResponse().getContentAsString();
        assertThat(content).isNotBlank();
        CategoryPriceRangeResponse response = objectMapper.readValue(content,
            CategoryPriceRangeResponse.class);
        assertThat(response.getCategoryName()).isEqualTo(categoryName);
        assertThat(response.getLowestPriceProduct().getBrandName()).isEqualTo("C");
        assertThat(response.getLowestPriceProduct().getPrice()).isEqualTo("10,000");
        assertThat(response.getHighestPriceProduct().getBrandName()).isEqualTo("I");
        assertThat(response.getHighestPriceProduct().getPrice()).isEqualTo("11,400");
    }
}