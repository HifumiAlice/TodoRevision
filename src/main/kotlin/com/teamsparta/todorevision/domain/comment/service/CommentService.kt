package com.teamsparta.todorevision.domain.comment.service

import com.sun.nio.sctp.IllegalReceiveException
import com.teamsparta.todorevision.domain.comment.dto.request.CommentCreateRequest
import com.teamsparta.todorevision.domain.comment.dto.request.CommentUpdateRequest
import com.teamsparta.todorevision.domain.comment.dto.response.CommentResponse
import com.teamsparta.todorevision.domain.comment.model.Comment
import com.teamsparta.todorevision.domain.comment.repository.CommentRepository
import com.teamsparta.todorevision.domain.member.model.Member
import com.teamsparta.todorevision.domain.member.repository.MemberRepository
import com.teamsparta.todorevision.domain.todo.model.Todo
import com.teamsparta.todorevision.domain.todo.repository.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentService(
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository,
    private val todoRepository: TodoRepository
) {

    fun createComment(todoId: Long, request: CommentCreateRequest, memberId: Long): CommentResponse {
        val todo: Todo = todoRepository.findByIdOrNull(todoId) ?: throw IllegalArgumentException("할일이 존재하지 않습니다.")
        val member: Member =
            memberRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("멤버가 존재하지 않습니다.")

        val comment: Comment = Comment(
            content = request.content,
            member = member
        )

        todo.addComment(comment)

        commentRepository.save(comment)

        return comment.toResponse()
    }

    fun updateComment(commentId: Long, request: CommentUpdateRequest, memberId: Long): CommentResponse {
        val comment : Comment = commentRepository.findByIdOrNull(commentId) ?: throw IllegalArgumentException("댓글이 없습니다.")
        val member : Member = memberRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("멤버가 없습니다.")

        if (comment.getMember().getId() != member.getId()){
            throw IllegalArgumentException("내가 작성한 댓글이 아닙니다. 수정이 불가능합니다.")
        }

        comment.updateContent(request.content)

        commentRepository.save(comment)

        return comment.toResponse()

    }

    fun deleteComment(commentId: Long, memberId: Long): Unit {
        val comment : Comment = commentRepository.findByIdOrNull(commentId) ?: throw IllegalArgumentException("댓글이 없습니다.")
        val member : Member = memberRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("멤버가 없습니다.")

        if (comment.getMember().getId() != member.getId()){
            throw IllegalArgumentException("내가 작성한 댓글이 아닙니다. 수정이 불가능합니다.")
        }

        commentRepository.delete(comment)

    }
}