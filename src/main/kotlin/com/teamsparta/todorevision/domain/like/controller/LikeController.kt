package com.teamsparta.todorevision.domain.like.controller

import com.teamsparta.todorevision.domain.like.dto.response.LikeResponse
import com.teamsparta.todorevision.domain.like.service.LikeService
import com.teamsparta.todorevision.infra.annotation.AuthenticationUserPrincipal
import com.teamsparta.todorevision.infra.aop.MemberDetails
import com.teamsparta.todorevision.infra.annotation.MemberPrincipal
import com.teamsparta.todorevision.infra.annotation.PreAuthorize
import com.teamsparta.todorevision.infra.resolver.UserPrincipal
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/likes")
class LikeController(
    private val likeService: LikeService
) {

    @PreAuthorize("USER")
    @PostMapping()
    fun createTodoLike(
        @RequestParam(name = "todoId") todoId: Long,
        @Parameter(hidden = true) @AuthenticationUserPrincipal userPrincipal: UserPrincipal?
    ): ResponseEntity<LikeResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(likeService.createTodoLike(todoId, userPrincipal?.id!!))
    }

    @PreAuthorize("USER")
    @DeleteMapping()
    fun deleteTodoLike(
        @RequestParam(name = "todoId") todoId: Long,
        @Parameter(hidden = true) @AuthenticationUserPrincipal userPrincipal: UserPrincipal?
    ) : ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(likeService.deleteTodoLike(todoId, userPrincipal?.id!!))
    }

}