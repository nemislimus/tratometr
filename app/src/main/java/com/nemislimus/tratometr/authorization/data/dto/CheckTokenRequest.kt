package com.nemislimus.tratometr.authorization.data.dto

import com.google.gson.annotations.SerializedName

data class CheckTokenRequest(
    @SerializedName("access_token")
    val accessToken: String
)
