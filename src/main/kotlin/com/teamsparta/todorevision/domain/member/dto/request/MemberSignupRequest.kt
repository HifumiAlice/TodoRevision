package com.teamsparta.todorevision.domain.member.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class MemberSignupRequest(
    @field:Email (message = "Email address must not be blank")
    val email: String,
    val nickname: String,
    val password: String,
    val passwordConfirmation: String
)
