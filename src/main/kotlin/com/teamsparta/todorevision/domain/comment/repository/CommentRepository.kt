package com.teamsparta.todorevision.domain.comment.repository

import com.teamsparta.todorevision.domain.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository


interface CommentRepository : JpaRepository<Comment, Long> {

}
