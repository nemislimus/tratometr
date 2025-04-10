package com.nemislimus.tratometr.di

import android.content.Context
import com.nemislimus.tratometr.authorization.data.AuthRepositoryImpl
import com.nemislimus.tratometr.authorization.data.TokensStorageRepositoryImpl
import com.nemislimus.tratometr.authorization.data.network.NetworkClient
import com.nemislimus.tratometr.authorization.domain.AuthInteractor
import com.nemislimus.tratometr.authorization.domain.AuthRepository
import com.nemislimus.tratometr.authorization.domain.TokensStorageInteractor
import com.nemislimus.tratometr.authorization.domain.TokensStorageRepository
import com.nemislimus.tratometr.authorization.domain.impl.AuthInteractorImpl
import com.nemislimus.tratometr.authorization.domain.impl.TokensStorageInteractorImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {
    @Singleton
    @Provides
    fun provideAuthInteractor(repository: AuthRepository): AuthInteractor {
        return AuthInteractorImpl(repository)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(client: NetworkClient): AuthRepository {
        return AuthRepositoryImpl(client)
    }

    @Singleton
    @Provides
    fun provideTokensStorageRepository(context: Context): TokensStorageRepository {
        return TokensStorageRepositoryImpl(context)
    }

    @Singleton
    @Provides
    fun provideTokensStorageInteractor(repository: TokensStorageRepository): TokensStorageInteractor {
        return TokensStorageInteractorImpl(repository)
    }
}