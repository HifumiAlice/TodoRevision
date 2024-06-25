package com.teamsparta.todorevision.domain.todo.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import com.teamsparta.todorevision.domain.todo.model.QTodo
import com.teamsparta.todorevision.domain.todo.model.Todo
import com.teamsparta.todorevision.infra.query.QueryDslSupport
import org.springframework.stereotype.Repository

@Repository
class TodoRepositoryImpl : CustomTodoRepository, QueryDslSupport() {

    private val todo = QTodo.todo

    override fun todoList(topic: String, keyword: String, orderBy: String, ascend: Boolean): List<Todo> {
        val booleanBuilder: BooleanBuilder = BooleanBuilder()

        if (topic.isNotEmpty()) {

            if (keyword.isEmpty()) throw IllegalArgumentException("Keyword can't be empty")

            when (topic) {
                "title" -> booleanBuilder.and(todo.title.containsIgnoreCase(keyword))
                "content" -> booleanBuilder.and(todo.content.containsIgnoreCase(keyword))
                "author" -> booleanBuilder.and(todo.member.profile.nickname.containsIgnoreCase(keyword))
            }
        }

        when (orderBy) {
            "title", "member", "createdAt" -> return queryFactory.select(todo).from(todo).where(booleanBuilder)
                .orderBy(getOrderSpecifier(todo, orderBy, ascend)).fetch()
            else -> throw IllegalArgumentException("정렬기준이 없습니다.")
        }

    }

    private fun getOrderSpecifier(path: EntityPathBase<*>, orderBy: String, ascend: Boolean): OrderSpecifier<*> {
        val pathBuilder = PathBuilder(path.type, path.metadata)

        return OrderSpecifier(
            if (ascend) Order.ASC else Order.DESC,
            pathBuilder.get(orderBy) as Expression<Comparable<*>>
        )
    }
}