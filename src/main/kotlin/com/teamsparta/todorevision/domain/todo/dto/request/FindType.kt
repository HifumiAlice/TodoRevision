package com.teamsparta.todorevision.domain.todo.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class FindType(
    @Schema(example = "null", required = false)
    val title: String,
    @Schema(example = "null", required = false)
    val content: String,
    @Schema(example = "null", required = false)
    val author: String,
)
