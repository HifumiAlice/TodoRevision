package com.teamsparta.todorevision.domain.like.repository

import com.teamsparta.todorevision.domain.like.model.Like
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository: JpaRepository<Like, Long> {

    fun existsByTodoIdAndMemberId(todoId: Long, memberId: Long): Boolean
    fun findByTodoIdAndMemberId(todoId: Long, memberId: Long): Like?
    fun findAllByMemberId(memberId: Long?): List<Like>
}
