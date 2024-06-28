package com.teamsparta.todorevision.domain.todo.controller

import com.teamsparta.todorevision.domain.todo.dto.request.FindType
import com.teamsparta.todorevision.domain.todo.dto.request.MyPageable
import com.teamsparta.todorevision.domain.todo.dto.request.TodoCreateRequest
import com.teamsparta.todorevision.domain.todo.dto.request.TodoUpdateRequest
import com.teamsparta.todorevision.domain.todo.dto.response.TodoResponse
import com.teamsparta.todorevision.domain.todo.dto.response.TodoWithCommentsResponse
import com.teamsparta.todorevision.domain.todo.service.TodoService
import com.teamsparta.todorevision.infra.annotation.AuthenticationUserPrincipal
import com.teamsparta.todorevision.infra.annotation.PreAuthorize
import com.teamsparta.todorevision.infra.resolver.UserPrincipal
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/todos")
class TodoController(
    private val todoService: TodoService,
) {
    @PreAuthorize("USER")
    @PostMapping()
    fun createTodo(
        @RequestBody request: TodoCreateRequest,
        @Parameter(hidden = true) @AuthenticationUserPrincipal userPrincipal: UserPrincipal?
    ): ResponseEntity<TodoResponse> {

        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(request, userPrincipal?.id!!))
    }

    @GetMapping()
    fun getTodos(
        @RequestParam(name = "topic", required = false) topic: String = "",
        @RequestParam(name = "keyword", required = false) keyword: String = "",
        @RequestParam(name = "order", required = false) order: String = "createdAt",
        @RequestParam(name = "ascend", required = false) ascend: Boolean = false,
        @Parameter(hidden = true) @AuthenticationUserPrincipal userPrincipal: UserPrincipal?
    ): ResponseEntity<List<TodoResponse>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(todoService.getTodos(topic, keyword, order, ascend, userPrincipal?.id))
    }

    @GetMapping("/{id}")
    fun getTodoById(
        @PathVariable(value = "id") todoId: Long,
        @Parameter(hidden = true) @AuthenticationUserPrincipal userPrincipal: UserPrincipal?
    ): ResponseEntity<TodoWithCommentsResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getTodoById(todoId, userPrincipal?.id))
    }

    @GetMapping("/page")
    fun getPageTodos(
        @ModelAttribute myPage: MyPageable,
        @Parameter(required = false) @ModelAttribute findType: FindType,
        @RequestParam(name = "date", required = false) date: LocalDate?,
        @RequestParam(name = "dateBefore", required = false) before: Boolean
    ): ResponseEntity<Page<TodoResponse>> {

        val sort: List<String> = myPage.sort.split(",")
        val direction: Direction = when (sort[1].uppercase()) {
            "ASC" -> Direction.ASC
            "DESC" -> Direction.DESC
            else -> Direction.DESC
        }
        val order: Order = Order(direction, sort[0])
        val pageNumber = if (myPage.pageNumber - 1 < 0) 0 else myPage.pageNumber - 1
        val pageRequest: PageRequest = PageRequest.of(pageNumber, myPage.size, Sort.by(order))

        return ResponseEntity.status(HttpStatus.OK).body(todoService.getPage(pageRequest, findType, date, before))
    }

    @PreAuthorize("USER")
    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable(value = "id") todoId: Long,
        @RequestBody request: TodoUpdateRequest,
        @Parameter(hidden = true) @AuthenticationUserPrincipal userPrincipal: UserPrincipal?
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodo(todoId, request, userPrincipal?.id!!))
    }

    @PreAuthorize("USER")
    @PatchMapping("/{id}")
    fun updateTodoDone(
        @PathVariable(value = "id") todoId: Long,
        @Parameter(hidden = true) @AuthenticationUserPrincipal userPrincipal: UserPrincipal?
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodoDone(todoId, userPrincipal?.id!!))
    }

    @PreAuthorize("USER")
    @DeleteMapping("/{id}")
    fun deleteTodo(
        @PathVariable(value = "id") todoId: Long,
        @Parameter(hidden = true) @AuthenticationUserPrincipal userPrincipal: UserPrincipal?
    ): ResponseEntity<Unit> {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(todoService.deleteTodo(todoId, userPrincipal?.id!!))
    }

}