package com.teamsparta.todorevision.domain.member.model

import com.teamsparta.todorevision.domain.member.dto.response.MemberResponse
import jakarta.persistence.*

@Entity
@Table(name = "todo")
class Member(
    @Column(name = "email", nullable = false)
    private var email: String,

    @Column(name = "password", nullable = false)
    private var password : String,

    @Embedded
    private val profile : Profile

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null


    fun getEmail() : String = email
    fun getProfile() : Profile = profile
    fun getPassword() : String = password
    fun getId() : Long? = id

    fun toResponse() : MemberResponse {
        return MemberResponse(
            memberId = id!!,
            account = email,
            nickname = profile.nickname
        )
    }
}


