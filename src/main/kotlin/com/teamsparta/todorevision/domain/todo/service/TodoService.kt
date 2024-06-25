package com.teamsparta.todorevision.domain.todo.service

import com.teamsparta.todorevision.domain.like.repository.LikeRepository
import com.teamsparta.todorevision.domain.member.model.Member
import com.teamsparta.todorevision.domain.member.repository.MemberRepository
import com.teamsparta.todorevision.domain.todo.dto.request.TodoCreateRequest
import com.teamsparta.todorevision.domain.todo.dto.request.TodoUpdateRequest
import com.teamsparta.todorevision.domain.todo.dto.response.TodoResponse
import com.teamsparta.todorevision.domain.todo.dto.response.TodoWithCommentsResponse
import com.teamsparta.todorevision.domain.todo.model.Todo
import com.teamsparta.todorevision.domain.todo.repository.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TodoService(
    private val todoRepository: TodoRepository,
    private val memberRepository: MemberRepository,
    private val likeRepository: LikeRepository,
) {
    fun createTodo(request: TodoCreateRequest, memberId: Long): TodoResponse {

        checkTitleAndContent(request.title, request.content)

        val member: Member =
            memberRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("멤버가 존재하지 않습니다. ${memberId}")

        val todo: Todo = Todo(
            title = request.title,
            content = request.content,
            member = member
        )

        todoRepository.save(todo)

        return todo.toResponse(false)
    }

    @Transactional(readOnly = true)
    fun getTodoById(todoId: Long, memberId: Long?): TodoWithCommentsResponse {

        val todo : Todo = todoRepository.findByIdOrNull(todoId)?: throw IllegalArgumentException("todo가 존재하지 않습니다. ${todoId}")

        if (memberId == null) {
            return todo.toWithCommentsResponse(false)
        }

        return todo.toWithCommentsResponse(likeRepository.existsByTodoIdAndMemberId(todo.getId()!!,memberId))
    }

    @Transactional(readOnly = true)
    fun getTodos(
        topic: String,
        keyword: String,
        orderBy: String,
        ascend: Boolean,
        memberId: Long?
    ): List<TodoResponse> {

        val likeTodoIds : List<Long> = likeRepository.findAllByMemberId(memberId).map {it.getTodoId()}
        val todos: List<Todo> = todoRepository.todoList(topic, keyword, orderBy, ascend)
        if (memberId == null) {
            return todos.map { it.toResponse(false) }
        }

        return todos.map { it.toResponse(likeTodoIds.find{ item->item==it.getId()!!} != null) }
    }

    fun updateTodo(todoId: Long, request: TodoUpdateRequest, memberId: Long): TodoResponse {

        val todo: Todo = validOwnTodo(todoId, memberId)

        if (todo.getDone()) {
            throw IllegalArgumentException("이미 완료된 할 일입니다. 수정할 수 없습니다.")
        }

        checkTitleAndContent(request.title, request.content)

        return todoRepository.save(todo.updateTitleAndContent(request.title, request.content))
            .toResponse(false)
    }

    fun updateTodoDone(todoId: Long, memberId: Long): TodoResponse {

        val todo: Todo = validOwnTodo(todoId, memberId)

        todo.updateDone()

        todoRepository.save(todo)

        return todo.toResponse(false) // 내거는 좋아요 불가
    }

    fun deleteTodo(todoId: Long, memberId: Long): Unit {

        val todo = validOwnTodo(todoId, memberId)

        todoRepository.delete(todo)
    }

    private fun checkTitleAndContent(title: String, content: String) {
        if (title.length !in 1..500) throw IllegalArgumentException("제목은 500자 까지 가능합니다.")
        if (content.length !in 0..5000) throw IllegalArgumentException("내용은 5000자 까지 가능합니다.")
    }

    private fun validOwnTodo(todoId: Long, memberId: Long): Todo {

        val todo: Todo =
            todoRepository.findByIdOrNull(todoId) ?: throw IllegalArgumentException("게시글이 존재하지 않습니다. ${todoId}")
        val member: Member =
            memberRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("멤버가 존재하지 않습니다. ${memberId}")

        if (todo.getMember().getId() != member.getId()) {
            throw IllegalArgumentException("자신의 게시글이 아닙니다.")
        }

        return todo
    }
}
