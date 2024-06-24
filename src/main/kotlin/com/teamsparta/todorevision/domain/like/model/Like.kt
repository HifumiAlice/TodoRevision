package com.teamsparta.todorevision.domain.like.model

import com.teamsparta.todorevision.domain.like.dto.response.LikeResponse
import jakarta.persistence.*

@Entity
@Table(name = "todo_like")
class Like(
    @Column(name = "member_id", nullable = false)
    var memberId: Long,

    @Column(name = "todo_id", nullable = false)
    var todoId: Long
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun toResponse() : LikeResponse {
        return LikeResponse(
            id = id!!
        )
    }
}
