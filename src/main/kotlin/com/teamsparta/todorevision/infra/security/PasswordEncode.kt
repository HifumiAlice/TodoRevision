package com.teamsparta.todorevision.infra.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Component
class PasswordEncode(
    @Value("\${auth.password.salt}")
    private val salt: String
) {

    fun encodePassword(password: String): String {
        val result = StringBuilder()

        try {
            val md: MessageDigest = MessageDigest.getInstance("SHA-256")
            md.update((password + salt).toByteArray())

            for (b in md.digest()) {
                result.append(String.format("%02x", b))
            }
            return result.toString()
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException("비밀번호 암호화에 실패함" + e.message)
        }
    }

}