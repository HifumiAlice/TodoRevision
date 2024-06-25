package com.teamsparta.todorevision.domain.todo.repository

import com.teamsparta.todorevision.domain.todo.model.Todo

interface CustomTodoRepository {

    fun todoList(topic : String, keyword: String, orderBy: String, ascend: Boolean) : List<Todo>
}