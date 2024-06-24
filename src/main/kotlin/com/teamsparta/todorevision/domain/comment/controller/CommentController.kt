package com.teamsparta.todorevision.domain.comment.controller

import com.teamsparta.todorevision.domain.comment.dto.request.CommentCreateRequest
import com.teamsparta.todorevision.domain.comment.dto.request.CommentUpdateRequest
import com.teamsparta.todorevision.domain.comment.dto.response.CommentResponse
import com.teamsparta.todorevision.domain.comment.service.CommentService
import com.teamsparta.todorevision.infra.aop.MemberDetails
import com.teamsparta.todorevision.infra.aop.MemberPrincipal
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/comments")
@RestController
class CommentController(
    private val commentService: CommentService
) {

    @MemberPrincipal("USER")
    @PostMapping("/{todoId}")
    fun createComment(
        @PathVariable todoId: Long,
        @Parameter(hidden = true) @ModelAttribute memberDetails : MemberDetails,
        @RequestBody request: CommentCreateRequest
    ): ResponseEntity<CommentResponse> {

        if (memberDetails.id == null) {
            throw IllegalArgumentException("인증/인가 안됨")
        }

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.createComment(todoId, request, memberDetails.id!!))
    }

    @MemberPrincipal("USER")
    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @Parameter(hidden = true) @ModelAttribute memberDetails : MemberDetails,
        @RequestBody request: CommentUpdateRequest
    ): ResponseEntity<CommentResponse> {

        if (memberDetails.id == null) {
            throw IllegalArgumentException("인증/인가 안됨")
        }

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.updateComment(commentId, request, memberDetails.id!!))
    }

    @MemberPrincipal("USER")
    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @Parameter(hidden = true) @ModelAttribute memberDetails : MemberDetails,
    ): ResponseEntity<Unit> {

        if (memberDetails.id == null) {
            throw IllegalArgumentException("인증/인가 안됨")
        }

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(commentService.deleteComment(commentId, memberDetails.id!!))
    }
}