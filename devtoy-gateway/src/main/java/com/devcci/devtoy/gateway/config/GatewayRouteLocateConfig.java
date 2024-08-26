package com.devcci.devtoy.gateway.config;

import com.devcci.devtoy.gateway.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GatewayRouteLocateConfig {

    private final String devtoyProductUri;
    private final String devtoyOrderUri;
    private final String devtoyMemberUri;

    public GatewayRouteLocateConfig(
        @Value("${service.uri.product}") String devtoyProductUri,
        @Value("${service.uri.order}") String devtoyOrderUri,
        @Value("${service.uri.member}") String devtoyMemberUri
    ) {
        this.devtoyProductUri = devtoyProductUri;
        this.devtoyOrderUri = devtoyOrderUri;
        this.devtoyMemberUri = devtoyMemberUri;
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder, AuthenticationFilter authenticationFilter) {
        return builder.routes()
            .route("swagger-route-product", r -> r.path("/v3/api-docs/product/**")
                .uri(devtoyProductUri))
            .route("swagger-route-member", r -> r.path("/v3/api-docs/member/**")
                .uri(devtoyMemberUri))
            .route("swagger-route-order", r -> r.path("/v3/api-docs/order/**")
                .uri(devtoyOrderUri))
            .route("auth-route", r -> r.path("/auth/**")
                .filters(f -> f.filter(authenticationFilter.apply(config -> config.setRoles(List.of("MEMBER", "ADMIN")))))
                .uri(devtoyMemberUri))
            .route("member-route", r -> r.path("/member/**")
                .filters(f -> f.filter(authenticationFilter.apply(config -> config.setRoles(List.of("MEMBER", "ADMIN")))))
                .uri(devtoyMemberUri))
            .route("product-route", r -> r.path("/product/**", "/brand/**")
                .filters(f -> f.filter(authenticationFilter.apply(config -> config.setRoles(List.of("MEMBER", "ADMIN")))))
                .uri(devtoyProductUri))
            .route("order-route", r -> r.path("/order/**")
                .filters(f -> f.filter(authenticationFilter.apply(config -> config.setRoles(List.of("MEMBER", "ADMIN")))))
                .uri(devtoyOrderUri))
            .build();
    }
}
