package com.teamsparta.todorevision.domain.like.controller

import com.teamsparta.todorevision.domain.like.dto.response.LikeResponse
import com.teamsparta.todorevision.domain.like.response.LikeService
import com.teamsparta.todorevision.infra.aop.MemberDetails
import com.teamsparta.todorevision.infra.aop.MemberPrincipal
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/likes")
class LikeController(
    private val likeService : LikeService
) {

    @MemberPrincipal("USER")
    @PostMapping()
    fun createTodoLike(
        @RequestParam(name = "todoId") todoId: Long,
        @RequestHeader headers: HttpHeaders,
        @Parameter(hidden = true) @ModelAttribute memberDetails: MemberDetails
    ): ResponseEntity<LikeResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(likeService.createTodoLike(todoId, memberDetails.id!!))
    }

}