package com.nemislimus.tratometr.authorization.domain.impl

import com.nemislimus.tratometr.authorization.domain.AuthInteractor
import com.nemislimus.tratometr.authorization.domain.AuthRepository
import com.nemislimus.tratometr.authorization.domain.models.Tokens

class AuthInteractorImpl(private val repository: AuthRepository) : AuthInteractor {
    override suspend fun register(email: String, password: String): Tokens {
        return repository.register(email, password)
    }

    override suspend fun login(email: String, password: String): Tokens {
        return repository.login(email, password)
    }
}