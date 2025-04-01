package com.nemislimus.tratometr.authorization.data.dto

import com.google.gson.annotations.SerializedName

data class CheckTokenResponse(
    @SerializedName("valid")
    val isValid: Boolean
): Response()
