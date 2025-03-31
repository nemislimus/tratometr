package com.nemislimus.tratometr.authorization.domain.models

data class LoginRequest(
    val email: String,
    val password: String
)
