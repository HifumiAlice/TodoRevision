package com.teamsparta.todorevision.infra.exception

import com.teamsparta.todorevision.infra.exception.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.badRequest().body(ErrorResponse(e.message!!))
    }

    @ExceptionHandler(SQLException::class)
    fun handleSQLException(e: SQLException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse("아니 왜 발생하냐고 ㅡㅡ\n${e.message}"))
    }
}