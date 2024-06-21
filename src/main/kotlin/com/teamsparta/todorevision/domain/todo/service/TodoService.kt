package com.teamsparta.todorevision.domain.todo.service

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

    fun createTodo(request: TodoCreateRequest): TodoResponse {
        // TODO 인증된 사용자만 가능
        checkTitleAndContent(request.title, request.content)

        return todoRepository.save(
            Todo(
                title = request.title,
                content = request.content,
                member = memberRepository.findByIdOrNull(2) ?: throw IllegalArgumentException("멤버가 존재하지 않습니다.")
            )
        ).toResponse()
    }

    fun getTodoById(todoId: Long): TodoResponse {
        return todoRepository.findByIdOrNull(todoId)?.toResponse() ?: throw IllegalArgumentException("todo가 존재하지 않습니다.")
    }

    fun getTodos(): List<TodoResponse> {
        return todoRepository.findAll().map { it.toResponse() }
    }

    fun updateTodo(todoId: Long, request: TodoUpdateRequest): TodoResponse {

        // TODO 인증 인가 완료 시 자기것만 처리 가능
        val todo: Todo = todoRepository.findByIdOrNull(todoId) ?: throw IllegalArgumentException("todo가 존재하지 않습니다.")

        checkTitleAndContent(request.title, request.content)

        return todoRepository.save(todo.updateTitleAndContent(request.title, request.content)).toResponse()
    }

    fun updateTodoDone(todoId: Long, done: Boolean): TodoResponse {
        // TODO 인증 인가 완료 시 자기것만 처리 가능
        val todo: Todo = todoRepository.findByIdOrNull(todoId) ?: throw IllegalArgumentException("todo가 존재하지 않습니다.")
        return todoRepository.save(todo.updateDone(done)).toResponse()
    }

    fun deleteTodo(todoId: Long): Unit {
        // TODO 인증 인가 완료 시 자기것만 처리 가능
        val todo: Todo = todoRepository.findByIdOrNull(todoId) ?: throw IllegalArgumentException("todo가 존재하지 않습니다.")
        todoRepository.delete(todo)
    }

    private fun checkTitleAndContent(title: String, content: String) {
        if (title.length !in 1..500) throw IllegalArgumentException("제목은 500자 까지 가능합니다.")
        if (content.length !in 0..5000) throw IllegalArgumentException("내용은 5000자 까지 가능합니다.")
    }
}
