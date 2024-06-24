package com.teamsparta.todorevision

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@EnableAspectJAutoProxy
@SpringBootApplication
class TodoRevisionApplication

fun main(args: Array<String>) {
    runApplication<TodoRevisionApplication>(*args)
}
