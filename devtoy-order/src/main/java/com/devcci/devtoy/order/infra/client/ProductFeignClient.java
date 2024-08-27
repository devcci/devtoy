package com.devcci.devtoy.order.infra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "DEVTOY-PRODUCT", configuration = FeignClientConfig.class)
public interface ProductFeignClient {

    @GetMapping("/product/bulk")
    List<ProductBulkResponse> getProductsByIds(@RequestParam("productIds") Set<Long> productIds);
}
