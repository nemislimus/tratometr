package com.nemislimus.tratometr.authorization.domain.models

import com.google.gson.annotations.SerializedName

data class CheckTokenResponse(
    @SerializedName("is_valid")
    val isValid: Boolean
)
