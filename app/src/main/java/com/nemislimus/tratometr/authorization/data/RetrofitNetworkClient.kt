package com.nemislimus.tratometr.authorization.data

import android.util.Log
import com.nemislimus.tratometr.authorization.data.dto.AuthResponse
import com.nemislimus.tratometr.authorization.data.dto.RegistrationRequest
import com.nemislimus.tratometr.authorization.data.dto.Response
import com.nemislimus.tratometr.authorization.data.network.ApiService
import com.nemislimus.tratometr.authorization.data.network.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
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

            /*is LoginRequest -> {

            }

            is RefreshTokenRequest -> {

            }

            is CheckTokenRequest -> {

            }*/
            else -> {
                return Response().apply { resultCode = 600 }
            }
        }
    }

    private suspend fun doRegistrationRequest(dto: RegistrationRequest): AuthResponse {
        try {
            val response = service.register(dto)
            if (response.isSuccessful){
                return response.body()!!
            } else {
                Log.d("Ошибка2", response.toString())
                return AuthResponse("","", 0).apply { resultCode = response.code() }
            }
        } catch (e: Exception) {
            Log.d("Ошибка1", e.toString())
            return AuthResponse("", "", 0).apply { resultCode = 1 }
        }
    }
}