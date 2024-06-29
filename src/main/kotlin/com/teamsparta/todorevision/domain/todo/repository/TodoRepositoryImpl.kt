package com.teamsparta.todorevision.domain.todo.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import com.teamsparta.todorevision.domain.todo.dto.request.FindType
import com.teamsparta.todorevision.domain.todo.model.QTodo
import com.teamsparta.todorevision.domain.todo.model.Todo
import com.teamsparta.todorevision.infra.query.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Repository
class TodoRepositoryImpl : CustomTodoRepository, QueryDslSupport() {

    private val todo: QTodo = QTodo.todo

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

    override fun todoPage(
        pageable: Pageable,
        types: FindType,
        date: LocalDate?,
        before: Boolean
    ): Page<Todo> {
        val whereBuilder: BooleanBuilder = BooleanBuilder()
        val typesBuilder: BooleanBuilder = BooleanBuilder()

        if (types.title != "null") {
            typesBuilder.or(todo.title.containsIgnoreCase(types.title))
        }
        if (types.content != "null") {
            typesBuilder.or(todo.content.containsIgnoreCase(types.content))
        }
        if (types.author != "null") {
            typesBuilder.or(todo.member.profile.nickname.eq(types.author))
        }

        if (date != null) {
            if (before) {
                whereBuilder.and(todo.createdAt.before(LocalDateTime.of(date, LocalTime.MIDNIGHT)))
            } else {
                whereBuilder.and(todo.createdAt.after(LocalDateTime.of(date, LocalTime.MIDNIGHT)))
            }
        }

        val totalCount: Long =
            queryFactory.select(todo.count()).from(todo).where(whereBuilder.and(typesBuilder)).fetchOne() ?: 0L

        val contents =
            queryFactory.select(todo).from(todo)
                .where(whereBuilder.and(typesBuilder))
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .orderBy(* getOrderPageSpecifire(pageable, todo))
                .fetch()

        return PageImpl(contents, pageable, totalCount)
    }

    private fun getOrderPageSpecifire(pageable: Pageable, path: EntityPathBase<*>): Array<OrderSpecifier<*>> {
        val path = PathBuilder(path.type, path.metadata)

        return pageable.sort.toList().map {
            OrderSpecifier(
                if (it.isAscending) Order.ASC else Order.DESC,
                path.get(it.property) as Expression<Comparable<*>>
            )
        }.toTypedArray()
    }
}