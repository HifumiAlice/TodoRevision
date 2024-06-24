package com.teamsparta.todorevision.infra.aop


@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class MemberPrincipal(
    val hasRole : String = ""
)