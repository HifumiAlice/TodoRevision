package com.teamsparta.todorevision.infra.swagger

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@SecurityScheme(
    type = SecuritySchemeType.HTTP,
    `in` = SecuritySchemeIn.HEADER,
    name = "Authorization",
    description = "Auth Token Header",
    bearerFormat = "JWT",
    scheme = "Bearer"
)

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .components(Components())
            .info(
                Info()
                    .title("Swagger API")
                    .description("나도 몰루?")
                    .version("v1.0.0")
            )
            .security(
                listOf(
                    SecurityRequirement().addList("Authorization")
                )
            )
    }
}