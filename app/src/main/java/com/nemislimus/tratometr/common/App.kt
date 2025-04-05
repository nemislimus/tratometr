package com.nemislimus.tratometr.common

import android.app.Application
import android.content.Context
import com.nemislimus.tratometr.di.AppComponent
import com.nemislimus.tratometr.di.DaggerAppComponent

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .setContextToComponent(this)
            .build()

    }
}

/** Поле расширения для удобного получения доступа к appComponent
  */
@Suppress("RecursivePropertyAccessor")
val Context.appComponent: AppComponent
    get() = when(this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }