package com.teamsparta.todorevision.domain.todo.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class MyPageable (
    @Schema(example = "1", description = "1부터 n 페이지 중 선택", defaultValue = "1")
    val pageNumber : Int,
    @Schema(example = "5", description = "한 페이지에 보여줄 개수", defaultValue = "5")
    val size : Int,
    @Schema(example = "createdAt,DESC", defaultValue = "createdAt,DESC", description = "정렬기준,ASC|DESC")
    val sort : String
)