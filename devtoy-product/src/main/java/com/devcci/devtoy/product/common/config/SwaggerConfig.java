package com.devcci.devtoy.product.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "[DEV TOY] 혼자 놀아보기", description = "서비스의 API 명세입니다.",
    version = "v1.0.0"))
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI();
    }
}
