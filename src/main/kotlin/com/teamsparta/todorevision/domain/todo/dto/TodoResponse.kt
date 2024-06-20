package com.teamsparta.todorevision.domain.todo.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class TodoResponse (
    val id: Long,
    val title: String,
    val content: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime,
    val memberId: Long,
    val done: Boolean
)
