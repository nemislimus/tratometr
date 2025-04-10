package com.nemislimus.tratometr.settings.domain.api

import com.nemislimus.tratometr.settings.domain.model.SettingsParams

interface SettingsRepository {
    suspend fun updateSettings(params: SettingsParams)
    suspend fun getSettings(): SettingsParams
}