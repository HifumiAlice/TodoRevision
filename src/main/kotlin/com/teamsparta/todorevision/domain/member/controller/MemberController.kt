package com.teamsparta.todorevision.domain.member.controller

import com.teamsparta.todorevision.domain.member.dto.request.MemberSignupRequest
import com.teamsparta.todorevision.domain.member.dto.response.MemberResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/members")
class MemberController {

    @PostMapping("signup")
    fun signup(@RequestBody request: MemberSignupRequest): ResponseEntity<MemberResponse> {
         TODO()
    }
}