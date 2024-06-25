package com.teamsparta.todorevision.domain.todo.model

import com.teamsparta.todorevision.domain.comment.model.Comment
import com.teamsparta.todorevision.domain.member.model.Member
import com.teamsparta.todorevision.domain.todo.dto.response.TodoResponse
import com.teamsparta.todorevision.domain.todo.dto.response.TodoWithCommentsResponse
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
    private var member: Member,

    @OneToMany(fetch = FetchType.LAZY ,cascade = [(CascadeType.ALL)], orphanRemoval = true)
    @JoinColumn(name = "todo_id", nullable = false)
    private val comments : MutableList<Comment> = mutableListOf()

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
    fun getComments(): MutableList<Comment> = this.comments

    fun toResponse(liked: Boolean): TodoResponse {
        return TodoResponse(
            id = id!!,
            title = title,
            content = content,
            createdAt = createdAt,
            done = done,
            member = member.toResponse(),
            liked = liked
        )
    }

    fun toWithCommentsResponse(liked: Boolean): TodoWithCommentsResponse {
        return TodoWithCommentsResponse(
            id = id!!,
            title = title,
            content = content,
            createdAt = createdAt,
            done = done,
            member = member.toResponse(),
            liked = liked,
            comments = comments.map {it.toResponse()}
        )

    }

    fun updateTitleAndContent(title: String, content: String): Todo {
        this.title = title
        this.content = content
        return this
    }

    fun updateDone(): Todo {
        done = !done
        return this
    }

    fun addComment(comment: Comment) {
        this.comments.add(comment)
    }

    fun deleteComment(comment: Comment) {
        this.comments.remove(comment)
    }
}