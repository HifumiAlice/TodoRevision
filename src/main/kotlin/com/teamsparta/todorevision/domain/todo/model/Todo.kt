package com.teamsparta.todorevision.domain.todo.model

import com.teamsparta.todorevision.domain.member.model.Member
import com.teamsparta.todorevision.domain.todo.dto.response.TodoResponse
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "todo")
class Todo(

    @Column(name = "title", nullable = false)
    private var title: String,

    @Column(name = "content", nullable = false)
    private var content: String,

    @Column(name = "created_at", nullable = false)
    private val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "done", nullable = false)
    private var done: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private var member: Member

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null


    fun getId(): Long? = this.id
    fun getTitle(): String = this.title
    fun getContent(): String = this.content
    fun getCreatedAt(): LocalDateTime = this.createdAt
    fun getDone(): Boolean = this.done
    fun getMember(): Member = this.member
    fun toResponse(): TodoResponse {
        return TodoResponse(
            id = id!!,
            title = title,
            content = content,
            createdAt = createdAt,
            done = done,
            member = member.toResponse()
        )

    }
}