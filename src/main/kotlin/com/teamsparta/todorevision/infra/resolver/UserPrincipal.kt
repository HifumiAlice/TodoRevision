package com.teamsparta.todorevision.infra.resolver

data class UserPrincipal(
    val id: Long,
    val email: String,
    val role: String
)
