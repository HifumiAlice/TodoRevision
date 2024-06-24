package com.teamsparta.todorevision.domain.todo.controller

import com.teamsparta.todorevision.domain.todo.dto.request.TodoCreateRequest
import com.teamsparta.todorevision.domain.todo.dto.request.TodoUpdateRequest
import com.teamsparta.todorevision.domain.todo.dto.response.TodoResponse
import com.teamsparta.todorevision.domain.todo.dto.response.TodoWithCommentsResponse
import com.teamsparta.todorevision.domain.todo.service.TodoService
import com.teamsparta.todorevision.infra.aop.MemberDetails
import com.teamsparta.todorevision.infra.aop.MemberPrincipal
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/todos")
class TodoController(
    private val todoService: TodoService,
) {

    @MemberPrincipal("USER")
    @PostMapping()
    fun createTodo(
        @RequestBody request: TodoCreateRequest,
        @RequestHeader headers: HttpHeaders,
        @Parameter(hidden = true) @ModelAttribute memberDetails : MemberDetails
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(request, memberDetails.id!!))
    }

    @GetMapping()
    fun getTodos(): ResponseEntity<List<TodoResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getTodos())
    }

    @GetMapping("/{id}")
    fun getTodoById(@PathVariable(value = "id") todoId: Long): ResponseEntity<TodoWithCommentsResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getTodoById(todoId))
    }

    @MemberPrincipal("USER")
    @PutMapping("/{id}")
    fun updateTodo(
        @RequestHeader headers: HttpHeaders,
        @PathVariable(value = "id") todoId: Long,
        @RequestBody request: TodoUpdateRequest,
        @Parameter(hidden = true) @ModelAttribute memberDetails : MemberDetails
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodo(todoId, request, memberDetails.id!!))
    }

    @MemberPrincipal("USER")
    @PatchMapping("/{id}")
    fun updateTodoDone(
        @RequestHeader headers: HttpHeaders,
        @PathVariable(value = "id") todoId: Long,
        @Parameter(hidden = true) @ModelAttribute memberDetails : MemberDetails
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodoDone(todoId, memberDetails.id!!))
    }

    @MemberPrincipal("USER")
    @DeleteMapping("/{id}")
    fun deleteTodo(
        @RequestHeader headers: HttpHeaders,
        @PathVariable(value = "id") todoId: Long,
        @Parameter(hidden = true) @ModelAttribute memberDetails : MemberDetails
    ): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(todoService.deleteTodo(todoId, memberDetails.id!!))
    }

}