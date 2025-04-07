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
import com.nemislimus.tratometr.authorization.domain.models.Tokens

class AuthRepositoryImpl(
    private val client: NetworkClient
) : AuthRepository {
    override suspend fun register(email: String, password: String): Tokens {
        val response = client.doAuthRequest(AuthRequest.RegistrationRequest(email, password))

        return when (response.resultCode) {
            -1 -> {
                Tokens(null, null, "Проверьте подключение к интернету")
            }

            201 -> {
                with(response as AuthResponse) {
                    Tokens(
                        accessToken,
                        refreshToken,
                        "Аккаунт успешно зарегистрирован"
                    )
                }
            }

            400 -> {
                Tokens(null, null, "Некорректный e-mail или пароль менее 7 символов")
            }

            409 -> {
                Tokens(null, null, "Пользователь с таким e-mail уже существует")
            }

            else -> {
                Tokens(null, null, "Неизвестная ошибка")
            }
        }
    }

    override suspend fun login(email: String, password: String): Tokens {
        val response = client.doAuthRequest(AuthRequest.LoginRequest(email, password))

        return when (response.resultCode) {
            -1 -> {
                Tokens(null, null, "Проверьте подключение к интернету")
            }

            200 -> with(response as AuthResponse) {
                Tokens(accessToken, refreshToken, "Успешный вход")
            }

            400 -> {
                Tokens(null, null, "Некорректный e-mail или пароль менее 7 символов")
            }

            else -> {
                Tokens(null, null, "Неизвестная ошибка")
            }
        }
    }

    override suspend fun refresh(token: String): Tokens {
        val response = client.doRefreshTokenRequest(RefreshTokenRequest(token))

        return when (response.resultCode) {
            -1 -> {
                Tokens(null, null, "Проверьте подключение к интернету")
            }

            200 -> with(response as RefreshTokenResponse) {
                Tokens(accessToken, refreshToken, "Токены успешно обновлены")
            }

            else -> {
                Tokens(null, null, "Неизвестная ошибка")
            }
        }
    }

    override suspend fun check(token: String): Boolean {
        val response = client.doCheckTokenRequest(CheckTokenRequest(token))

        return when (response.resultCode) {
            200 -> with(response as CheckTokenResponse) {
                isValid
            }

            else -> {
                false
            }
        }
    }

    override suspend fun recovery(email: String) {
        val response = client.doRecoveryRequest(RecoveryRequest(email))

        when (response.resultCode) {
            200 -> {
                Log.d("Восстановление пароля", "Восстановлен")
            }

            else -> {
                Log.d("Восстановление пароля", "Что-то пошло не так")
            }
        }
    }
}