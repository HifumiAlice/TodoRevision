package com.teamsparta.todorevision.domain.comment.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.teamsparta.todorevision.domain.comment.dto.response.CommentResponse
import com.teamsparta.todorevision.domain.member.dto.response.MemberResponse
import com.teamsparta.todorevision.domain.member.model.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
class Comment(

    @Column(name = "content", nullable = false)
    private var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private var member: Member


) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null

    @Column(name = "create_at", nullable = false)
    private var createdAt: LocalDateTime = LocalDateTime.now()

    fun getContent(): String = content
    fun getCreatedAt(): LocalDateTime = createdAt
    fun getMember(): Member = member
    fun getId(): Long? = id

    fun updateContent(content: String) {
        this.content = content
    }

    fun toResponse(): CommentResponse {
        return CommentResponse(
            commentId = id!!,
            content = content,
            createdAt = createdAt,
            member = member.toResponse()
        )
    }


}