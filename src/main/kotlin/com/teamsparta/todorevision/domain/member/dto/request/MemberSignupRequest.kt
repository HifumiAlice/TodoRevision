package com.teamsparta.todorevision.domain.member.dto.request

data class MemberSignupRequest(
    val email: String,
    val nickname: String,
    val password: String,
    val passwordConfirmation: String
)
