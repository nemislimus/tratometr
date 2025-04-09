package com.nemislimus.tratometr.di

import android.content.Context
import com.nemislimus.tratometr.authorization.data.RetrofitNetworkClient
import com.nemislimus.tratometr.authorization.data.network.NetworkClient
import com.nemislimus.tratometr.expenses.data.database.DBConverter
import com.nemislimus.tratometr.expenses.data.database.DBHelper
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
    @Singleton
    @Provides
    fun provideNetworkClient(context: Context): NetworkClient {
        return RetrofitNetworkClient(context)
    }
}