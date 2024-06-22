package com.teamsparta.todorevision.domain.todo.service

import com.teamsparta.todorevision.domain.member.model.Member
import com.teamsparta.todorevision.domain.member.repository.MemberRepository
import com.teamsparta.todorevision.domain.todo.dto.request.TodoCreateRequest
import com.teamsparta.todorevision.domain.todo.dto.request.TodoUpdateRequest
import com.teamsparta.todorevision.domain.todo.dto.response.TodoResponse
import com.teamsparta.todorevision.domain.todo.model.Todo
import com.teamsparta.todorevision.domain.todo.repository.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TodoService(
    private val todoRepository: TodoRepository,
    private val memberRepository: MemberRepository
) {
    fun createTodo(request: TodoCreateRequest, memberId: Long): TodoResponse {
        // TODO 인증된 사용자만 가능
        // TODO: 나중에 memberId 제외하고 헤더에서 jwt를 통해서 값 받기

        checkTitleAndContent(request.title, request.content)

        val member: Member =
            memberRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("멤버가 존재하지 않습니다.")
        val todo: Todo = Todo(
            title = request.title,
            content = request.content,
            member = member
        )

        todoRepository.save(todo)

        return todo.toResponse()
    }

    @Transactional(readOnly = true)
    fun getTodoById(todoId: Long): TodoResponse {
        return todoRepository.findByIdOrNull(todoId)?.toResponse() ?: throw IllegalArgumentException("todo가 존재하지 않습니다.")
    }

    @Transactional(readOnly = true)
    fun getTodos(): List<TodoResponse> {
        // TODO 검색기능 나중에 추가하기
        // TODO 정렬기능 나중에 추가하기
        return todoRepository.findAll().map { it.toResponse() }
    }

    fun updateTodo(todoId: Long, request: TodoUpdateRequest, memberId: Long): TodoResponse {
        // TODO 인증 인가 완료 시 자기것만 처리 가능
        // TODO: 나중에 memberId 제외하고 헤더에서 jwt를 통해서 값 받기

        val todo: Todo = todoRepository.findByIdOrNull(todoId) ?: throw IllegalArgumentException("todo가 존재하지 않습니다.")
        val member: Member =
            memberRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("멤버가 존재하지 않습니다.")

        if (todo.getMember().getId() != member.getId()) {
            throw IllegalStateException("자기 Todo가 아닙니다. 수정이 불가능합니다.")
        }

        if (todo.getDone()) {
            throw IllegalArgumentException("이미 완료된 할 일입니다. 수정할 수 없습니다.")
        }
        
        checkTitleAndContent(request.title, request.content)

        return todoRepository.save(todo.updateTitleAndContent(request.title, request.content)).toResponse()
    }

    fun updateTodoDone(todoId: Long, done: Boolean, memberId: Long): TodoResponse {
        // TODO 인증 인가 완료 시 자기것만 처리 가능
        // TODO: 나중에 memberId 제외하고 헤더에서 jwt를 통해서 값 받기

        val todo: Todo = todoRepository.findByIdOrNull(todoId) ?: throw IllegalArgumentException("todo가 존재하지 않습니다.")
        val member: Member =
            memberRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("멤버가 존재하지 않습니다.")

        if (todo.getMember().getId() != member.getId()) {
            throw IllegalArgumentException("자기 Todo가 아닙니다. 수정이 불가능합니다.")
        }

        todo.updateDone(done)

        todoRepository.save(todo)

        return todo.toResponse()
    }

    fun deleteTodo(todoId: Long, memberId: Long): Unit {
        // TODO 인증 인가 완료 시 자기것만 처리 가능
        // TODO: 나중에 memberId 제외하고 헤더에서 jwt를 통해서 값 받기
        val todo: Todo = todoRepository.findByIdOrNull(todoId) ?: throw IllegalArgumentException("todo가 존재하지 않습니다.")
        val member: Member =
            memberRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("멤버가 존재하지 않습니다.")

        if (todo.getMember().getId() != member.getId()) {
            throw IllegalArgumentException("자기 Todo가 아닙니다. 삭제가 불가능합니다.")
        }

        todoRepository.delete(todo)
    }

    private fun checkTitleAndContent(title: String, content: String) {
        if (title.length !in 1..500) throw IllegalArgumentException("제목은 500자 까지 가능합니다.")
        if (content.length !in 0..5000) throw IllegalArgumentException("내용은 5000자 까지 가능합니다.")
    }
}
