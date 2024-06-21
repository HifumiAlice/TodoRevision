package com.teamsparta.todorevision.domain.todo.controller

import com.teamsparta.todorevision.domain.todo.dto.request.TodoCreateRequest
import com.teamsparta.todorevision.domain.todo.dto.request.TodoUpdateRequest
import com.teamsparta.todorevision.domain.todo.dto.response.TodoResponse
import com.teamsparta.todorevision.domain.todo.service.TodoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/todos")
class TodoController(
    private val todoService: TodoService
) {

    @PostMapping()
    fun createTodo(@RequestBody request: TodoCreateRequest): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(request))
    }

    @GetMapping()
    fun getTodos(): ResponseEntity<List<TodoResponse>> {
        TODO()
    }

    @GetMapping("/{id}")
    fun getTodoById(@PathVariable(value = "id") todoId: Long): ResponseEntity<TodoResponse> {
        TODO()
    }

    @PutMapping("/{id}")
    fun updateTodo(@PathVariable(value = "id") todoId: Long, request: TodoUpdateRequest): ResponseEntity<TodoResponse> {
        TODO()
    }

    @PatchMapping("/{id}")
    fun updateTodoDone(@PathVariable(value = "id") todoId: Long, done: Boolean): ResponseEntity<TodoResponse> {
        TODO()
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable(value = "id") todoId: Long): ResponseEntity<Unit> {
        TODO()
    }

}