package com.nemislimus.tratometr.authorization.data

import android.content.Context
import android.net.ConnectivityManager
import com.nemislimus.tratometr.authorization.data.dto.AuthRequest
import com.nemislimus.tratometr.authorization.data.dto.AuthResponse
import com.nemislimus.tratometr.authorization.data.dto.CheckTokenRequest
import com.nemislimus.tratometr.authorization.data.dto.CheckTokenResponse
import com.nemislimus.tratometr.authorization.data.dto.RecoveryRequest
import com.nemislimus.tratometr.authorization.data.dto.RefreshTokenRequest
import com.nemislimus.tratometr.authorization.data.dto.RefreshTokenResponse
import com.nemislimus.tratometr.authorization.data.dto.Response
import com.nemislimus.tratometr.authorization.data.network.ApiService
import com.nemislimus.tratometr.authorization.data.network.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {
    private val empty = ""
    private val baseUrl = "http://130.193.44.66:8080/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: ApiService = retrofit.create(ApiService::class.java)

    override suspend fun doAuthRequest(dto: AuthRequest): AuthResponse {
        return try {
            val response = when (dto) {
                is AuthRequest.RegistrationRequest -> service.register(dto)
                is AuthRequest.LoginRequest -> service.login(dto)
            }
            if (!isConnected()) {
                Response().apply { resultCode = -1 }
            }
            if (response.isSuccessful) {
                response.body()!!.apply { resultCode = response.code() }
            } else {
                AuthResponse(empty, empty, 0).apply { resultCode = response.code() }
            }
        } catch (e: Exception) {
            AuthResponse(empty, empty, 0).apply { resultCode = 500 }
        }
    }

    override suspend fun doCheckTokenRequest(dto: CheckTokenRequest): CheckTokenResponse {
        return try {
            val authHeader = "Bearer ${dto.accessToken}"
            val response = service.checkToken(authHeader)
            if (!isConnected()) {
                Response().apply { resultCode = -1 }
            }

            if (response.isSuccessful) {
                response.body()?.apply {
                    resultCode = response.code()
                }
                    ?: CheckTokenResponse(false).apply { resultCode = 404 }
            } else {
                CheckTokenResponse(false).apply { resultCode = response.code() }
            }
        } catch (e: Exception) {
            CheckTokenResponse(false).apply { resultCode = 500 }
        }
    }

    override suspend fun doRefreshTokenRequest(dto: RefreshTokenRequest): RefreshTokenResponse {
        return try {
            val response = service.refreshToken(dto)
            if (!isConnected()) {
                Response().apply { resultCode = -1 }
            }
            if (response.isSuccessful) {
                response.body()!!.apply { resultCode = response.code() }
            } else {
                RefreshTokenResponse(empty, empty).apply { resultCode = response.code() }
            }
        } catch (e: Exception) {
            RefreshTokenResponse(empty, empty).apply { resultCode = 500 }
        }
    }

    override suspend fun doRecoveryRequest(dto: RecoveryRequest): Response {
        return try {
            val response = service.recoverPassword(dto.email)
            if (!isConnected()) {
                Response().apply { resultCode = -1 }
            }
            if (response.isSuccessful) {
                Response().apply { resultCode = response.code() }
            } else {
                Response().apply { resultCode = response.code() }
            }
        } catch (e: Exception) {
            Response().apply { resultCode = 500 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.isActiveNetworkMetered
    }
}