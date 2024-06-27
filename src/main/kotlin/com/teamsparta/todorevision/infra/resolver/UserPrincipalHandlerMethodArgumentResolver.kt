package com.teamsparta.todorevision.infra.resolver

import com.teamsparta.todorevision.infra.annotation.AuthenticationUserPrincipal
import com.teamsparta.todorevision.infra.security.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class UserPrincipalHandlerMethodArgumentResolver(
//    private val jwtService: JwtService

) : HandlerMethodArgumentResolver {
    @Autowired
    private lateinit var jwtService: JwtService

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val annotation: Boolean = parameter.hasParameterAnnotation(AuthenticationUserPrincipal::class.java)
        val argument: Boolean = parameter.parameterType == UserPrincipal::class.java
        return annotation && argument
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val token: String = validateToken(webRequest.getHeader("Authorization")) ?: return null

        jwtService.validateToken(token).onSuccess {
            val id: Long = it.payload.subject.toLong()
            val email: String = it.payload.get("email", String::class.java)
            val role: String = it.payload["role", String::class.java]

            val userPrincipal = UserPrincipal(id, email, role)
            return userPrincipal
        }

        return null
    }

    private fun validateToken(token: String?): String? {
        token ?: return null
        return token.substring(7)
    }
}