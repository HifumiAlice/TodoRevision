package com.teamsparta.todorevision.domain.member.service

import com.teamsparta.todorevision.domain.member.dto.request.MemberLoginRequest
import com.teamsparta.todorevision.domain.member.dto.request.MemberSignupRequest
import com.teamsparta.todorevision.domain.member.dto.response.LoginResponse
import com.teamsparta.todorevision.domain.member.dto.response.MemberResponse
import com.teamsparta.todorevision.domain.member.model.Member
import com.teamsparta.todorevision.domain.member.model.Profile
import com.teamsparta.todorevision.domain.member.repository.MemberRepository
import com.teamsparta.todorevision.infra.security.JwtService
import com.teamsparta.todorevision.infra.security.PasswordEncode
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val jwtService: JwtService,
    private val passwordEncode: PasswordEncode
) {

    fun signup(request: MemberSignupRequest): MemberResponse {

        validateSignupRequest(request.email, request.nickname, request.password, request.passwordConfirmation)

        val member: Member = Member(
            email = request.email,
            profile = Profile(
                nickname = request.nickname,
            ),
            password = passwordEncode.encodePassword(request.password)
        )

        memberRepository.save(member)

        return member.toResponse()
    }

    private fun validateSignupRequest(email: String, nickname: String, pw1: String, pw2: String) {

        if (pw1.length in 4..15) {
            if (nickname in pw1) {
                throw IllegalArgumentException("비밀번호 안에 닉네임이 포함될 수 없습니다.")
            }
            if (pw1 != pw2) {
                throw IllegalArgumentException("비밀번호를 확인해주세요")
            }
        } else {
            throw IllegalArgumentException("비밀번호가 너무 짧거나 깁니다.\n4자리 이상 15자리 이하로 입력해주세요")
        }

        if (nickname.length in 3..10) {
            nickname.forEach {
                if (it !in 'a'..'z' && it !in 'A'..'Z' && it !in '0'..'9')
                    throw IllegalArgumentException("닉네임 에러")
            }
        }

        if (memberRepository.existsByEmail(email)) {
            throw IllegalArgumentException("이메일이 이미 존재합니다.")
        } else if (memberRepository.existsByProfile(Profile(nickname))) {
            throw IllegalArgumentException("닉네임이 이미 존재합니다.")
        }
    }

    fun login(request: MemberLoginRequest): LoginResponse {
        val member: Member =
            memberRepository.findByEmail(request.email) ?: throw IllegalArgumentException("존재하지 않는 이메일입니다.")

        if (member.getPassword() != passwordEncode.encodePassword(request.password)) {
            throw IllegalArgumentException("비밀번호가 틀립니다.")
        }

        val id = member.getId()!!
        val email = member.getEmail()
        val nickname = member.getProfile().getNickname()
        val role = member.getRole()

        val accessToken = jwtService.generateToken(id, email, nickname, role)

        return LoginResponse(accessToken)
    }


}