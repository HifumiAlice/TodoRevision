package com.teamsparta.todorevision.domain.member.repository

import com.teamsparta.todorevision.domain.member.model.Member
import com.teamsparta.todorevision.domain.member.model.Profile
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun existsByEmail(nickname: String): Boolean
    fun existsByProfile(profile: Profile): Boolean
    fun findByEmail(email: String): Member?
}