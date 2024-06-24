package com.teamsparta.todorevision.infra.aop

import com.teamsparta.todorevision.infra.security.JwtService
import io.jsonwebtoken.security.SignatureException
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
class ValidToken(
    private val jwtService: JwtService
) {

    fun isValidToken(headers: HttpHeaders): Long {

        val token = headers["Authorization"]?.get(0) ?: throw IllegalArgumentException("헤더에 토큰 없음")

        if ("Bearer " != token.slice(IntRange(0, 6))) {
            throw IllegalArgumentException("토큰 형식이 이상함 Beare이 없음")
        } else {
            try {
                val test = jwtService.validateToken(token.substring("Bearer ".length))
                return test.payload.subject.toLong()
            } catch (e: SignatureException) {
                throw IllegalArgumentException("토큰이 유효하지 않음")
            }
        }
    }
}