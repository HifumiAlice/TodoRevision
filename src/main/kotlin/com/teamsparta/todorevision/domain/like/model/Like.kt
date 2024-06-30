package com.teamsparta.todorevision.domain.like.model

import com.teamsparta.todorevision.domain.like.dto.response.LikeResponse
import jakarta.persistence.*

@Entity
@Table(name = "todo_like")
class Like(
    @Column(name = "member_id", nullable = false)
    private var memberId: Long,

    @Column(name = "todo_id", nullable = false)
    private var todoId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null
) {

    fun toResponse(): LikeResponse {
        return LikeResponse(
            id = id!!
        )
    }

    fun getMemberId(): Long = memberId
    fun getTodoId(): Long = todoId
    fun getId(): Long? = id
}
