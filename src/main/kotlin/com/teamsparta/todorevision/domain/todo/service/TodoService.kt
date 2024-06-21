package com.teamsparta.todorevision.domain.todo.service

import com.teamsparta.todorevision.domain.member.repository.MemberRepository
import com.teamsparta.todorevision.domain.todo.dto.request.TodoCreateRequest
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
        checkTitleAndContent(request.title, request.content)

        return todoRepository.save(
            Todo(
                title = request.title,
                content = request.content,
                member = memberRepository.findByIdOrNull(2) ?: throw IllegalArgumentException("멤버가 존재하지 않습니다.")
            )
        ).toResponse()
    }

    fun getTodoById(todoId: Long) : TodoResponse {
        TODO()
    }

    fun getTodos() : List<TodoResponse> {
        TODO()
    }

    fun updateTodo() : TodoResponse {
        TODO()
    }

    fun updateTodoDone() : TodoResponse {
        TODO()
    }

    fun deleteTodo() : Unit {
        TODO()
    }

    private fun checkTitleAndContent(title: String, content: String) {
        if (title.length !in 1..500) throw IllegalArgumentException("제목은 500자 까지 가능합니다.")
        if (content.length !in 0..5000) throw IllegalArgumentException("내용은 5000자 까지 가능합니다.")
    }
}
