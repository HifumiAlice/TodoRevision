package com.teamsparta.todorevision.infra.exception

import com.teamsparta.todorevision.infra.exception.dto.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.badRequest().body(ErrorResponse(e.message!!))
    }
}