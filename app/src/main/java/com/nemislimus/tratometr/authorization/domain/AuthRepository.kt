package com.nemislimus.tratometr.authorization.domain

import com.nemislimus.tratometr.authorization.domain.models.Tokens

interface AuthRepository {
    suspend fun register(email: String, password: String): Tokens
    //suspend fun login(email: String, password: String): Tokens
}