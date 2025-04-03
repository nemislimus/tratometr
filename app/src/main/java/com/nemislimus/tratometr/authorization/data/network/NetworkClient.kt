package com.nemislimus.tratometr.authorization.data.network

import com.nemislimus.tratometr.authorization.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}