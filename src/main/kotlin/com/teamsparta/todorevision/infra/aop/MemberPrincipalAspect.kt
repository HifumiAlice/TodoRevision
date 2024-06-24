package com.teamsparta.todorevision.infra.aop

import com.teamsparta.todorevision.infra.security.JwtService
import io.jsonwebtoken.security.SignatureException
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Aspect
@Component
class MemberPrincipalAspect(
    private val jwtService: JwtService,
) {

    @Pointcut("@annotation(com.teamsparta.todorevision.infra.aop.MemberPrincipal)")
    private fun cut() {}

    @Before("@annotation(com.teamsparta.todorevision.infra.aop.MemberPrincipal)")
    fun before(joinPoint: JoinPoint) {
        val args : Array<Any> = joinPoint.args

        var header : HttpHeaders = HttpHeaders()
        var memberDetails: MemberDetails = MemberDetails()

        for (data in args) {
            if (data is HttpHeaders) {
                header = data
            }
            if (data is MemberDetails) {
                memberDetails = data
            }
        }

        val token = header["Authorization"]?.get(0) ?: throw IllegalArgumentException("헤더에 토큰 없음")

        if ("Bearer " != token.slice(IntRange(0, 6))) {
            throw IllegalArgumentException("토큰 형식이 이상함 Beare이 없음")
        } else {
            try {
                val test = jwtService.validateToken(token.substring("Bearer ".length))
                memberDetails.id = test.payload.subject.toLong()
                memberDetails.email = test.payload["email", String::class.java]
                memberDetails.role = test.payload["role", String::class.java]
            } catch (e: SignatureException) {
                throw IllegalArgumentException("토큰이 유효하지 않음")
            }
        }

    }
}

