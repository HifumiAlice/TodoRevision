package com.teamsparta.todorevision.domain.member.service

import com.teamsparta.todorevision.domain.member.dto.request.MemberSignupRequest
import com.teamsparta.todorevision.domain.member.dto.response.MemberResponse
import com.teamsparta.todorevision.domain.member.model.Member
import com.teamsparta.todorevision.domain.member.model.Profile
import com.teamsparta.todorevision.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    fun signup(request: MemberSignupRequest): MemberResponse {

        checkSignupRequest(request.email, request.nickname, request.password, request.passwordConfirmation)
        return memberRepository.save(
            Member(
                email = request.email,
                profile = Profile(
                    nickname = request.nickname,
                ),
                password = request.password
            )
        ).toResponse()
    }

    private fun checkSignupRequest(email: String, nickname: String, pw1: String, pw2: String) {

        if (pw1.length in 4..15) {
            if (nickname in pw1) throw IllegalArgumentException("비밀번호 안에 닉네임이 포함될 수 없습니다.")
            if (pw1 != pw2) throw IllegalArgumentException("비밀번호를 확인해주세요")
        } else throw IllegalArgumentException("비밀번호가 너무 짧거나 깁니다.\n4자리 이상 15자리 이하로 입력해주세요")

        if (nickname.length in 3..10) {
            nickname.forEach {
                if (it !in 'a'..'z' || it !in 'A'..'Z' || it !in '0'..'9')
                    throw IllegalArgumentException("닉네임 에러")
            }
        }

        if (memberRepository.existsByEmail(nickname) || memberRepository.existsByProfile(Profile(nickname)))
            throw IllegalArgumentException("이메일 또는 닉네임이 이미 존재합니다.")
    }


}