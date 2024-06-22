package com.teamsparta.todorevision.domain.member.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Profile (
    @Column(name = "nickname", nullable = false)
    var nickname: String
)
