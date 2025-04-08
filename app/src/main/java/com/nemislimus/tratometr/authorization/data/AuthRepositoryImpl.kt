package com.nemislimus.tratometr.authorization.data

import android.util.Log
import com.nemislimus.tratometr.authorization.data.dto.AuthRequest
import com.nemislimus.tratometr.authorization.data.dto.AuthResponse
import com.nemislimus.tratometr.authorization.data.dto.CheckTokenRequest
import com.nemislimus.tratometr.authorization.data.dto.CheckTokenResponse
import com.nemislimus.tratometr.authorization.data.dto.RecoveryRequest
import com.nemislimus.tratometr.authorization.data.dto.RefreshTokenRequest
import com.nemislimus.tratometr.authorization.data.dto.RefreshTokenResponse
import com.nemislimus.tratometr.authorization.data.network.NetworkClient
import com.nemislimus.tratometr.authorization.domain.AuthRepository
import com.nemislimus.tratometr.authorization.domain.models.Resource
import com.nemislimus.tratometr.authorization.domain.models.Tokens

class AuthRepositoryImpl(
    private val client: NetworkClient
) : AuthRepository {
    companion object {
        const val ERROR_NETWORK = "Проверьте подключение к интернету"
        const val ERROR_INVALID_CREDENTIALS = "Некорректный e-mail или пароль менее 7 символов"
        const val ERROR_EMAIL_EXISTS = "Пользователь с таким e-mail уже существует"
        const val ERROR_UNKNOWN = "Неизвестная ошибка"
    }

    override suspend fun register(email: String, password: String): Resource<Tokens> {
        val response = client.doAuthRequest(AuthRequest.RegistrationRequest(email, password))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error<Tokens>(ERROR_NETWORK)
            }

            201 -> {
                with(response as AuthResponse) {
                    Resource.Success<Tokens>(Tokens(accessToken, refreshToken))
                }
            }

            400 -> {
                Resource.Error<Tokens>(ERROR_INVALID_CREDENTIALS)
            }

            409 -> {
                Resource.Error<Tokens>(ERROR_EMAIL_EXISTS)
            }

            else -> {
                Resource.Error<Tokens>(ERROR_UNKNOWN)
            }
        }
    }

    override suspend fun login(email: String, password: String): Resource<Tokens> {
        val response = client.doAuthRequest(AuthRequest.LoginRequest(email, password))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error<Tokens>(ERROR_NETWORK)
            }

            200 -> with(response as AuthResponse) {
                Resource.Success<Tokens>(Tokens(accessToken, refreshToken))
            }

            400 -> {
                Resource.Error<Tokens>(ERROR_INVALID_CREDENTIALS)
            }

            else -> {
                Resource.Error<Tokens>(ERROR_UNKNOWN)
            }
        }
    }

    override suspend fun refresh(token: String): Resource<Tokens> {
        val response = client.doRefreshTokenRequest(RefreshTokenRequest(token))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error<Tokens>(ERROR_NETWORK)
            }

            200 -> with(response as RefreshTokenResponse) {
                Resource.Success<Tokens>(Tokens(accessToken, refreshToken))
            }

            else -> {
                Resource.Error<Tokens>(ERROR_UNKNOWN)
            }
        }
    }

    override suspend fun check(token: String): Resource<Boolean> {
        val response = client.doCheckTokenRequest(CheckTokenRequest(token))

        return when (response.resultCode) {
            200 -> with(response as CheckTokenResponse) {
                Resource.Success<Boolean>(isValid)
            }

            else -> {
                Resource.Error<Boolean>(ERROR_UNKNOWN, false)
            }
        }
    }

    override suspend fun recovery(email: String): Resource<Boolean> {
        val response = client.doRecoveryRequest(RecoveryRequest(email))

        return when (response.resultCode) {
            200 -> {
                Resource.Success<Boolean>(true)
            }

            else -> {
                Resource.Error<Boolean>(ERROR_UNKNOWN, false)
            }
        }
    }
}