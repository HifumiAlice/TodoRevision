package com.teamsparta.todorevision.domain.comment.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.teamsparta.todorevision.domain.member.dto.response.MemberResponse
import java.time.LocalDateTime

data class CommentResponse(
    val commentId: Long,
    val content: String,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul", shape = JsonFormat.Shape.STRING)
    val createdAt: LocalDateTime,
    val member: MemberResponse,
)
