package com.teamsparta.todorevision.infra.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI() : OpenAPI {
        return OpenAPI()
            .components(Components())
            .info(
                Info()
                    .title("Swagger API")
                    .description("나도 몰루?")
                    .version("v1.0.0")
            )
    }
}