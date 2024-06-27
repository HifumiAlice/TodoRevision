package com.teamsparta.todorevision.infra.resolver

import com.teamsparta.todorevision.infra.security.JwtService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfigure(
//    private val jwtService: JwtService,
): WebMvcConfigurer {

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(userPrincipalHandlerMethodArgumentResolver())
    }

    @Bean
    fun userPrincipalHandlerMethodArgumentResolver(): UserPrincipalHandlerMethodArgumentResolver {
        return UserPrincipalHandlerMethodArgumentResolver()
    }
}