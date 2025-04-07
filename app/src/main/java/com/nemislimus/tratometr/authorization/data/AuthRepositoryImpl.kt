package com.nemislimus.tratometr.authorization.data

import com.nemislimus.tratometr.authorization.data.dto.AuthRequest
import com.nemislimus.tratometr.authorization.data.dto.AuthResponse
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
                        response.accessToken,
                        response.refreshToken,
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
}