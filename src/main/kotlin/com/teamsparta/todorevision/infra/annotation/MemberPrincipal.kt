package com.teamsparta.todorevision.infra.annotation


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MemberPrincipal(
    val hasRole : String = ""
)