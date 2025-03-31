package com.nemislimus.tratometr.authorization.data.network

interface NetworkClient {
    suspend fun doRequest(dto: Any)
}