package com.teamsparta.todorevision.domain.member.dto.request

import jakarta.validation.constraints.Email

data class MemberLoginRequest (
    @field:Email(message = "Email address must not be blank")
    val email: String,
    val password: String
)
