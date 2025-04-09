package com.nemislimus.tratometr.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class, PresentationModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun setContextToComponent(context: Context): Builder
        fun build(): AppComponent
    }
}