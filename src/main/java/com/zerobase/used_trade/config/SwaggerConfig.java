package com.zerobase.used_trade.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .components(new Components())
        .info(apiInfo())
        .addSecurityItem(securityRequirement())
        .components(components());
  }

  private Info apiInfo() {
    return new Info()
        .title("중고 거래 서비스")
        .description("중고 상품 거래, 위탁 판매를 도와주는 서비스")
        .version("1.0.0");
  }

  //인증정보 리스트
  private SecurityRequirement securityRequirement() {
    return new SecurityRequirement()
        .addList("signUserId");
  }

  private SecurityScheme signUserId() {
    return new SecurityScheme().type(Type.APIKEY)
        .in(In.HEADER)
        .name("Authorization");
  }

  private Components components() {
    return new Components()
        .addSecuritySchemes("signUserId", signUserId());
  }
}
