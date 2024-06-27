package com.teamsparta.todorevision.domain.member.controller

import com.teamsparta.todorevision.domain.member.dto.request.MemberLoginRequest
import com.teamsparta.todorevision.domain.member.dto.request.MemberSignupRequest
import com.teamsparta.todorevision.domain.member.dto.response.LoginResponse
import com.teamsparta.todorevision.domain.member.dto.response.MemberResponse
import com.teamsparta.todorevision.domain.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService
) {

    @PostMapping("signup")
    fun signup(@Valid @RequestBody request: MemberSignupRequest): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(memberService.signup(request))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: MemberLoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.login(request))
    }
}