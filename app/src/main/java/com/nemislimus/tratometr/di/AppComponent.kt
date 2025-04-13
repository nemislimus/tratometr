package com.nemislimus.tratometr.di

import android.content.Context
import com.nemislimus.tratometr.authorization.ui.fragment.AuthorizationFragment
import com.nemislimus.tratometr.authorization.ui.fragment.RegistrationFragment
import com.nemislimus.tratometr.authorization.ui.fragment.SplashFragment
import com.nemislimus.tratometr.expenses.ui.fragment.CreateExpenseFragment
import com.nemislimus.tratometr.expenses.ui.fragment.ExpensesFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class, PresentationModule::class])
interface AppComponent {
    fun inject(fragment: SplashFragment)
    fun inject(fragment: AuthorizationFragment)
    fun inject(fragment: RegistrationFragment)
    fun inject(fragment: CreateExpenseFragment)
    fun inject(fragment: ExpensesFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun setContextToComponent(context: Context): Builder
        fun build(): AppComponent
    }
}