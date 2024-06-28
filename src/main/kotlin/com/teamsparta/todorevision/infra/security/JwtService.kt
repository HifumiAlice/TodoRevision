package com.teamsparta.todorevision.infra.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.*

@Component
class JwtService(
    @Value("\${auth.jwt.issuer}")
    private val issuer: String,

    @Value("\${auth.jwt.secret}")
    private val secretKey: String,

    @Value("\${auth.jwt.expired}")
    private val expired: Long // 168시간 7일,

) {
    fun validateToken(token: String): Result<Jws<Claims>> {
        return kotlin.runCatching {
            val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
        }

    }

    fun generateToken(id: Long, email: String, nickname: String, role: String): String {
        val header: Map<String, Any> = mapOf<String, Any>("typ" to "JWT", "alg" to "HS256") // 더이상 안쓰나봄
        val payLoads: Map<String, Any> = mapOf<String, Any>("email" to email, "nickname" to nickname, "role" to role)

        val now = Instant.now()
        val exp = Date.from(now.plusMillis(expired * 60L * 60L * 1000L))  // now + expired * 60L * 60L * 1000L // 밀리초

        val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

        val jwt = Jwts.builder()
            .issuer(issuer)
            .subject(id.toString())
            .claims(payLoads)
            .issuedAt(Date.from(now))
            .expiration(exp)
            .signWith(key)
            .compact()

        return jwt
    }

}