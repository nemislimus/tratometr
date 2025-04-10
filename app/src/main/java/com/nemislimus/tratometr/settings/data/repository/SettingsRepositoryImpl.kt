package com.nemislimus.tratometr.settings.data.repository

import com.nemislimus.tratometr.settings.data.storage.SettingsStorage
import com.nemislimus.tratometr.settings.data.storage.mapper.SettingsMapper
import com.nemislimus.tratometr.settings.domain.api.SettingsRepository
import com.nemislimus.tratometr.settings.domain.model.SettingsParams

class SettingsRepositoryImpl(
    private val storage: SettingsStorage
): SettingsRepository {

    override suspend fun updateSettings(params: SettingsParams) {
        storage.update(
            SettingsMapper.map(params)
        )
    }

    override suspend fun getSettings(): SettingsParams {
        return SettingsMapper.map(
            storage.get()
        )
    }
}