package com.teamsparta.todorevision.domain.member.model

import com.teamsparta.todorevision.domain.member.dto.response.MemberResponse
import jakarta.persistence.*

@Entity
@Table(name = "member")
class Member(
    @Column(name = "email", nullable = false)
    private var email: String,

    @Column(name = "password", nullable = false)
    private var password: String,

    @Embedded
    private val profile: Profile
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null

    @Column(name = "role", nullable = false)
    private var role: String = "USER"

    fun getEmail(): String = email
    fun getProfile(): Profile = profile
    fun getPassword(): String = password
    fun getId(): Long? = id
    fun getRole(): String = role

    fun toResponse(): MemberResponse {
        return MemberResponse(
            memberId = id!!,
            nickname = profile.getNickname()
        )
    }
}


