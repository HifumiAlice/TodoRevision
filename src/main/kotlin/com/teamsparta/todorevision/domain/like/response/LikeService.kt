package com.teamsparta.todorevision.domain.like.response

import com.teamsparta.todorevision.domain.like.dto.response.LikeResponse
import com.teamsparta.todorevision.domain.like.model.Like
import com.teamsparta.todorevision.domain.like.repository.LikeRepository
import com.teamsparta.todorevision.domain.member.model.Member
import com.teamsparta.todorevision.domain.member.repository.MemberRepository
import com.teamsparta.todorevision.domain.todo.model.Todo
import com.teamsparta.todorevision.domain.todo.repository.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class LikeService(
    private val memberRepository: MemberRepository,
    private val todoRepository: TodoRepository,
    private val likeRepository: LikeRepository
) {

    fun createTodoLike(todoId: Long, memberId: Long): LikeResponse {

        val todo: Todo = todoRepository.findByIdOrNull(todoId) ?: throw IllegalArgumentException("게시물이 없습니다.")
        val member: Member = memberRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("멤버가 없습니다.")

        if (todo.getMember().getId() == member.getId()) {
            throw IllegalArgumentException("자기 게시물은 좋아요를 누를 수 없습니다.")
        }

        if (likeRepository.existsByTodoIdAndMemberId(todoId, memberId)) {
            throw IllegalArgumentException("이미 좋아요를 눌렀습니다.")
        }

        val like = Like(memberId = member.getId()!!, todoId = todo.getId()!!)

        likeRepository.save(like)

        return like.toResponse()
    }


}