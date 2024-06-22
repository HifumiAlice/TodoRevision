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
    fun createTodo(
        @RequestBody request: TodoCreateRequest,
        @RequestParam(name = "memberId") memberId: Long
    ): ResponseEntity<TodoResponse> {
        // TODO: 나중에 memberId 제외하고 헤더에서 jwt를 통해서 값 받기

        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(request, memberId))
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
    fun updateTodo(
        @PathVariable(value = "id") todoId: Long,
        request: TodoUpdateRequest,
        @RequestParam(name = "memberId") memberId: Long
    ): ResponseEntity<TodoResponse> {
        // TODO: 나중에 memberId 제외하고 헤더에서 jwt를 통해서 값 받기
        return try{
            ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodo(todoId, request, memberId))
        } catch(e: IllegalStateException){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

    }

    @PatchMapping("/{id}")
    fun updateTodoDone(
        @PathVariable(value = "id") todoId: Long,
        @RequestParam(name = "memberId") memberId: Long
    ): ResponseEntity<TodoResponse> {
        // TODO: 나중에 memberId 제외하고 헤더에서 jwt를 통해서 값 받기
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodoDone(todoId, memberId))
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(
        @PathVariable(value = "id") todoId: Long,
        @RequestParam(name = "memberId") memberId: Long
    ): ResponseEntity<Unit> {
        // TODO: 나중에 memberId 제외하고 헤더에서 jwt를 통해서 값 받기
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(todoService.deleteTodo(todoId, memberId))
    }

}