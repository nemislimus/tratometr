package com.nemislimus.tratometr.di

import android.content.Context
import com.nemislimus.tratometr.common.MainActivity
import com.nemislimus.tratometr.settings.data.storage.SettingsStorage
import com.nemislimus.tratometr.settings.domain.api.SettingsRepository
import com.nemislimus.tratometr.settings.ui.fragment.SettingsFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class, PresentationModule::class])
interface AppComponent {

    fun inject(fragment: SettingsFragment)

    fun getSettingsRepository(): SettingsRepository

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun setContextToComponent(context: Context): Builder
        fun build(): AppComponent
    }

}