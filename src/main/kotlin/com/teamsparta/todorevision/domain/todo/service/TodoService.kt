package com.teamsparta.todorevision.domain.todo.service

import com.teamsparta.todorevision.domain.like.repository.LikeRepository
import com.teamsparta.todorevision.domain.member.model.Member
import com.teamsparta.todorevision.domain.member.repository.MemberRepository
import com.teamsparta.todorevision.domain.todo.dto.request.FindType
import com.teamsparta.todorevision.domain.todo.dto.request.TodoCreateRequest
import com.teamsparta.todorevision.domain.todo.dto.request.TodoUpdateRequest
import com.teamsparta.todorevision.domain.todo.dto.response.TodoResponse
import com.teamsparta.todorevision.domain.todo.dto.response.TodoWithCommentsResponse
import com.teamsparta.todorevision.domain.todo.model.Todo
import com.teamsparta.todorevision.domain.todo.repository.TodoRepository
import com.teamsparta.todorevision.infra.exception.ModelNotFoundException
import com.teamsparta.todorevision.infra.exception.UnAuthorizeException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

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

        val todo: Todo =
            todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)

        if (memberId == null) {
            return todo.toWithCommentsResponse(false)
        }

        return todo.toWithCommentsResponse(likeRepository.existsByTodoIdAndMemberId(todo.getId()!!, memberId))
    }

    @Transactional(readOnly = true)
    fun getTodos(
        topic: String,
        keyword: String,
        orderBy: String,
        ascend: Boolean,
        memberId: Long?
    ): List<TodoResponse> {

        val likeTodoIds: List<Long> = likeRepository.findAllByMemberId(memberId).map { it.getTodoId() }
        val todos: List<Todo> = todoRepository.todoList(topic, keyword, orderBy, ascend)

        if (memberId == null) {
            return todos.map { it.toResponse(false) }
        }

        return todos.map { it.toResponse(likeTodoIds.find { item -> item == it.getId()!! } != null) }
    }

    fun getPage(
        myPage: Pageable,
        findType: FindType,
        date: LocalDate?,
        before: Boolean,
        memberId: Long?
    ): Page<TodoResponse> {
        val page: Page<Todo> = todoRepository.todoPage(myPage, findType, date, before)
        val likeTodoIds = likeRepository.findAllByMemberId(memberId).map { it.getTodoId() }
        return page.map { it.toResponse(likeTodoIds.find { item -> item == it.getId()!! } != null) }
    }

    fun updateTodo(todoId: Long, request: TodoUpdateRequest, memberId: Long): TodoResponse {

        val todo: Todo =
            todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)

        if (!checkMyTodo(todo.getMember().getId()!!, memberId)) {
            throw UnAuthorizeException("자신의 게시글이 아닙니다. 수정할 수 업습니다.")
        }

        if (todo.getDone()) {
            throw IllegalStateException("이미 완료된 할 일입니다. 수정할 수 없습니다.")
        }

        checkTitleAndContent(request.title, request.content)

        todoRepository.save(todo.updateTitleAndContent(request.title, request.content))

        return todo.toResponse(false)
    }

    fun updateTodoDone(todoId: Long, memberId: Long): TodoResponse {

        val todo: Todo =
            todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)

        if (!checkMyTodo(todo.getMember().getId()!!, memberId)) {
            throw UnAuthorizeException("자신의 게시글이 아닙니다. 수정할 수 업습니다.")
        }


        todo.updateDone()

        todoRepository.save(todo)

        return todo.toResponse(false) // 내거는 좋아요 불가
    }

    fun deleteTodo(todoId: Long, memberId: Long): Unit {

        val todo: Todo =
            todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)

        if (!checkMyTodo(todo.getMember().getId()!!, memberId)) {
            throw UnAuthorizeException("자신의 게시글이 아닙니다. 삭제할 수 없습니다.")
        }


        todoRepository.delete(todo)
    }

    private fun checkTitleAndContent(title: String, content: String) {
        if (title.length !in 1..500) throw IllegalArgumentException("제목은 500자 까지 가능합니다.")
        if (content.length !in 0..5000) throw IllegalArgumentException("내용은 5000자 까지 가능합니다.")
    }

    private fun checkMyTodo(todoMemberId: Long, memberId: Long): Boolean {

        val member: Member =
            memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId)

        return todoMemberId == member.getId()
    }


}
