package com.teamsparta.todorevision.domain.todo.repository

import com.teamsparta.todorevision.domain.todo.dto.request.FindType
import com.teamsparta.todorevision.domain.todo.model.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface CustomTodoRepository {

    fun todoList(topic: String, keyword: String, orderBy: String, ascend: Boolean): List<Todo>

    fun todoPage(pageable: Pageable, types: FindType, date: LocalDate?, before: Boolean): Page<Todo>
}