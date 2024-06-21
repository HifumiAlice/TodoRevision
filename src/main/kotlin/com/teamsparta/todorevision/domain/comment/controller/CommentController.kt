package com.teamsparta.todorevision.domain.comment.controller

import com.teamsparta.todorevision.domain.comment.dto.request.CommentCreateRequest
import com.teamsparta.todorevision.domain.comment.dto.request.CommentUpdateRequest
import com.teamsparta.todorevision.domain.comment.dto.response.CommentResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/comments")
@RestController
class CommentController {

    @PostMapping("/{todoId}")
    fun createComment(
        @PathVariable todoId: Long,
        @RequestBody request: CommentCreateRequest
    ): ResponseEntity<CommentResponse> {
        TODO()
    }

    @PutMapping("/{commentId}")
    fun updateComment(@PathVariable commentId: Long, @RequestBody request : CommentUpdateRequest) : ResponseEntity<CommentResponse> {
        TODO()
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: Long): ResponseEntity<Unit> {
        TODO()
    }
}