package com.swyp.project.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
	info = @Info(title = "My API", version = "v1"),
	security = {@SecurityRequirement(name = "bearerAuth")} // 전역으로 JWT 적용
)
@SecurityScheme(
	name = "bearerAuth",            // SecurityRequirement에서 참조하는 이름
	type = SecuritySchemeType.HTTP, // HTTP 기반 인증
	scheme = "bearer",              // Bearer 토큰 방식
	bearerFormat = "JWT"            // JWT 형식 명시
)
public class SwaggerConfig {
}
