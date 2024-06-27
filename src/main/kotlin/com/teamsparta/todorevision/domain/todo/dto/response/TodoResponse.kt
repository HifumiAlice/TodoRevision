package com.teamsparta.todorevision.domain.todo.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.teamsparta.todorevision.domain.member.dto.response.MemberResponse
import java.time.LocalDateTime

data class TodoResponse(
    val id: Long,
    val title: String,
    val content: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime,
    val done: Boolean,
    val member: MemberResponse,
    val liked : Boolean
)
