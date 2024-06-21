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
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getTodos())
    }

    @GetMapping("/{id}")
    fun getTodoById(@PathVariable(value = "id") todoId: Long): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getTodoById(todoId))
    }

    @PutMapping("/{id}")
    fun updateTodo(@PathVariable(value = "id") todoId: Long, request: TodoUpdateRequest): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodo(request))
    }

    @PatchMapping("/{id}")
    fun updateTodoDone(@PathVariable(value = "id") todoId: Long, done: Boolean): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodoDone(done))
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable(value = "id") todoId: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(todoService.deleteTodo())
    }

}