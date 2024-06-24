package com.teamsparta.todorevision.infra.aop

import com.teamsparta.todorevision.infra.security.JwtService
import io.jsonwebtoken.security.SignatureException
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Aspect
@Component
class MemberPrincipalAspect(
    private val jwtService: JwtService,
) {

    @Before("@annotation(com.teamsparta.todorevision.infra.aop.MemberPrincipal)")
    fun before(joinPoint: JoinPoint) {
        val args: Array<Any> = joinPoint.args

        val methodSignature : MethodSignature = joinPoint.signature as MethodSignature
        val method : Method = methodSignature.method as Method
        val annotation : MemberPrincipal = method.getAnnotation(MemberPrincipal::class.java)

        var header: HttpHeaders? = null
        var memberDetails: MemberDetails? = null

        for (data in args) {
            if (data is HttpHeaders) {
                header = data
            }
            if (data is MemberDetails){
                memberDetails = data
            }
        }

        if (header == null || memberDetails == null) {
            return
        }

        // 토큰이 없다는 것은 로그인을 안 했다는 것
        val token = header["Authorization"]?.get(0) ?: run {
            authorize(annotation, memberDetails)
            return
        }

        if (!token.startsWith("Bearer ")) {
            return
        }

        try {
            jwtService.validateToken(token.substring("Bearer ".length)).let {
                memberDetails.id = it.payload.subject.toLong()
                memberDetails.email = it.payload["email", String::class.java]
                memberDetails.role = it.payload["role", String::class.java]
            }
            authorize(annotation, memberDetails)

        } catch (e: SignatureException) {
            throw IllegalArgumentException("토큰이 유효하지 않음")
        }

    }

    private fun authorize(annotation : MemberPrincipal, memberDetails: MemberDetails) {

        if (annotation.hasRole.isNotEmpty()) {

            val hasRole = annotation.hasRole.split(" ")

            if (memberDetails.role !in hasRole ) {
                throw IllegalArgumentException("API 실행할 권한이 없음")
            }
        }
    }
}


