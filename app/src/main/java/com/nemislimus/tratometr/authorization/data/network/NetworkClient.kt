package com.nemislimus.tratometr.authorization.data.network

import com.nemislimus.tratometr.authorization.data.dto.AuthRequest
import com.nemislimus.tratometr.authorization.data.dto.Response

interface NetworkClient {
    suspend fun doAuthRequest(dto: AuthRequest): Response
    fun isConnected(): Boolean
}