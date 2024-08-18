package com.devcci.devtoy.gateway.config;

import com.devcci.devtoy.gateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GatewayRouteLocateConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder, AuthenticationFilter authenticationFilter) {
        return builder.routes()
            .route("swagger-route-product", r -> r.path("/v3/api-docs/product/**")
                .uri("lb://DEVTOY-PRODUCT"))
            .route("swagger-route-member", r -> r.path("/v3/api-docs/member/**")
                .uri("lb://DEVTOY-MEMBER"))
            .route("auth-route", r -> r.path("/auth/**")
                .uri("lb://DEVTOY-MEMBER"))
            .route("member-route", r -> r.path("/member/**")
                .filters(f -> f.filter(authenticationFilter.apply(config -> config.setRoles(List.of("MEMBER", "ADMIN")))))
                .uri("lb://DEVTOY-MEMBER"))
            .route("product-route", r -> r.path("/product/**", "/brand/**")
                .filters(f -> f.filter(authenticationFilter.apply(config -> config.setRoles(List.of("MEMBER", "ADMIN")))))
                .uri("lb://DEVTOY-PRODUCT"))
            .build();
    }
}
