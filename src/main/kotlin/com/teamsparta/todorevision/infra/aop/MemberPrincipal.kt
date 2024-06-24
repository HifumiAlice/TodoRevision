package com.teamsparta.todorevision.infra.aop


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MemberPrincipal(
    val hasRole : String = ""
)