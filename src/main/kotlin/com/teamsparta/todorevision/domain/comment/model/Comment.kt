package com.teamsparta.todorevision.domain.comment.model

import com.teamsparta.todorevision.domain.member.model.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
class Comment(

    @Column(name = "content", nullable = false)
    private var content : String,

    @Column(name = "create_at", nullable = false)
    private var createdAt : LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private var member: Member


    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id : Long? = null

    fun getContent() : String = content
    fun getCreatedAt() : LocalDateTime = createdAt
    fun getMember() : Member = member
    fun getId() : Long? = id

    fun updateComment(comment: Comment) {
        this.content = comment.content
    }


}