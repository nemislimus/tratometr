package com.nemislimus.tratometr.authorization.data

import android.util.Log
import com.nemislimus.tratometr.authorization.data.dto.AuthResponse
import com.nemislimus.tratometr.authorization.data.dto.CheckTokenRequest
import com.nemislimus.tratometr.authorization.data.dto.CheckTokenResponse
import com.nemislimus.tratometr.authorization.data.dto.LoginRequest
import com.nemislimus.tratometr.authorization.data.dto.RefreshTokenRequest
import com.nemislimus.tratometr.authorization.data.dto.RefreshTokenResponse
import com.nemislimus.tratometr.authorization.data.dto.RegistrationRequest
import com.nemislimus.tratometr.authorization.data.dto.Response
import com.nemislimus.tratometr.authorization.data.network.ApiService
import com.nemislimus.tratometr.authorization.data.network.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val empty = ""
    private val baseUrl = "http://130.193.44.66:8080/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: ApiService = retrofit.create(ApiService::class.java)

    override suspend fun doRequest(dto: Any): Response {
        when (dto) {
            is RegistrationRequest -> {
                return doRegistrationRequest(dto)
            }

            is LoginRequest -> {
                return doLoginRequest(dto)
            }

            is RefreshTokenRequest -> {
                return doRefreshTokenRequest(dto)
            }

            is CheckTokenRequest -> {
                return doCheckTokenRequest(dto)
            }

            else -> {
                return Response().apply { resultCode = 100500 }
            }
        }
    }

    private suspend fun doRegistrationRequest(dto: RegistrationRequest): AuthResponse {
        return try {
            val response = service.register(dto)
            if (response.isSuccessful) {
                response.body()!!.apply { resultCode = response.code() }
            } else {
                AuthResponse(empty, empty, 0).apply { resultCode = response.code() }
            }
        } catch (e: Exception) {
            AuthResponse(empty, empty, 0).apply { resultCode = 1 }
        }
    }

    private suspend fun doLoginRequest(dto: LoginRequest): AuthResponse {
        return try {
            val response = service.login(dto)
            if (response.isSuccessful) {
                response.body()!!.apply { resultCode = response.code() }
            } else {
                AuthResponse(empty, empty, 0).apply { resultCode = response.code() }
            }
        } catch (e: Exception) {
            AuthResponse(empty, empty, 0).apply { resultCode = 1 }
        }
    }

    private suspend fun doCheckTokenRequest(dto: CheckTokenRequest): CheckTokenResponse {
        return try {
            val authHeader = "Bearer ${dto.accessToken}"
            val response = service.checkToken(authHeader)

            if (response.isSuccessful) {
                response.body()?.apply {
                    resultCode = response.code()
                    Log.d("Check token", "Успешно")
                }
                    ?: CheckTokenResponse(false).apply { resultCode = 404 }
            } else {
                CheckTokenResponse(false).apply { resultCode = response.code() }
            }
        } catch (e: Exception) {
            CheckTokenResponse(false).apply { resultCode = 1 }
        }
    }

    private suspend fun doRefreshTokenRequest(dto: RefreshTokenRequest): RefreshTokenResponse {
        return try {
            val response = service.refreshToken(dto)
            if (response.isSuccessful) {
                response.body()!!.apply { resultCode = response.code() }
            } else {
                RefreshTokenResponse(empty, empty).apply { resultCode = response.code() }
            }
        } catch (e: Exception) {
            Log.d("Refresh", e.toString())
            RefreshTokenResponse(empty, empty).apply { resultCode = 1 }
        }
    }
}