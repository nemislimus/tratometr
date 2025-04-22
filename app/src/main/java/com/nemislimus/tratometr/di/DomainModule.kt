package com.nemislimus.tratometr.di

import android.content.Context
import com.nemislimus.tratometr.analytics.data.db.dao.AnalyticsDao
import com.nemislimus.tratometr.analytics.data.db.dao.AnalyticsRepositoryImpl
import com.nemislimus.tratometr.analytics.domain.api.AnalyticsRepository
import com.nemislimus.tratometr.authorization.data.AuthRepositoryImpl
import com.nemislimus.tratometr.authorization.data.TokensStorageRepositoryImpl
import com.nemislimus.tratometr.authorization.data.network.NetworkClient
import com.nemislimus.tratometr.authorization.domain.AuthInteractor
import com.nemislimus.tratometr.authorization.domain.AuthRepository
import com.nemislimus.tratometr.authorization.domain.TokensStorageInteractor
import com.nemislimus.tratometr.authorization.domain.TokensStorageRepository
import com.nemislimus.tratometr.authorization.domain.impl.AuthInteractorImpl
import com.nemislimus.tratometr.authorization.domain.impl.TokensStorageInteractorImpl
import com.nemislimus.tratometr.expenses.data.database.DBConverter
import com.nemislimus.tratometr.expenses.data.database.ExpenseHistoryDao
import com.nemislimus.tratometr.expenses.data.database.ExpenseHistoryRepositoryImpl
import com.nemislimus.tratometr.expenses.domain.api.ExpenseHistoryInteractor
import com.nemislimus.tratometr.expenses.domain.api.ExpenseHistoryRepository
import com.nemislimus.tratometr.expenses.domain.impl.ExpenseHistoryInteractorImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class DomainModule {

    ///////////////////////////// INTERACTOR SECTION
    @Provides
    fun provideAuthInteractor(repository: AuthRepository): AuthInteractor {
        return AuthInteractorImpl(repository)
    }

    @Provides
    fun provideExpenseHistoryInteractor(repository: ExpenseHistoryRepository): ExpenseHistoryInteractor {
        return ExpenseHistoryInteractorImpl(repository)
    }

    ///////////////////////////// INTERACTOR SECTION end

    ///////////////////////////// REPOSITORY SECTION

    @Singleton
    @Provides
    fun provideAuthRepository(client: NetworkClient): AuthRepository {
        return AuthRepositoryImpl(client)
    }

    @Singleton
    @Provides
    fun provideExpenseHistoryRepository(expenseHistoryDao: ExpenseHistoryDao, dbConverter: DBConverter): ExpenseHistoryRepository {
        return ExpenseHistoryRepositoryImpl(expenseHistoryDao, dbConverter)
    }

    @Singleton
    @Provides
    fun provideAnalyticsRepository(analyticsDao: AnalyticsDao, dbConverter: DBConverter): AnalyticsRepository {
        return AnalyticsRepositoryImpl(analyticsDao, dbConverter)
    }

    ///////////////////////////// REPOSITORY SECTION end

    ///////////////////////////// STORAGE SECTION

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

    ///////////////////////////// STORAGE SECTION end
}