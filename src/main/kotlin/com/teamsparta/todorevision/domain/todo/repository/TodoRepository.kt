package com.teamsparta.todorevision.domain.todo.repository

import com.teamsparta.todorevision.domain.todo.model.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<Todo, Long>, CustomTodoRepository {

}
