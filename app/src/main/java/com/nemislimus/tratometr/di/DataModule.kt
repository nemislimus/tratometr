package com.nemislimus.tratometr.di

import android.content.Context
import com.nemislimus.tratometr.expenses.data.database.DBConverter
import com.nemislimus.tratometr.expenses.data.database.DBHelper
import com.nemislimus.tratometr.settings.data.repository.SettingsRepositoryImpl
import com.nemislimus.tratometr.settings.data.storage.SettingsStorage
import com.nemislimus.tratometr.settings.data.storage.impl.SharedPrefsSettingsStorage
import com.nemislimus.tratometr.settings.domain.api.SettingsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    // DATABASE SECTION
    @Singleton
    @Provides
    fun provideDataBaseHelper(context: Context): DBHelper {
        return DBHelper(context)
    }

    @Singleton
    @Provides
    fun provideDataBaseConverter(): DBConverter = DBConverter()

    // NETWORK SECTION

    // SETTINGS SECTION
    @Singleton
    @Provides
    fun provideSettingStorage(context: Context): SettingsStorage {
        return SharedPrefsSettingsStorage(context)
    }

    @Singleton
    @Provides
    fun provideSettingsRepository(storage: SettingsStorage): SettingsRepository {
        return SettingsRepositoryImpl(storage)
    }

}