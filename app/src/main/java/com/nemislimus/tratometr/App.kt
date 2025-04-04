package com.nemislimus.tratometr

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

        appContext = this@App
    }

    companion object {
        lateinit var appContext: Context
    }
}

// Для удобного получения компонента сделаем экстеншн:
@Suppress("RecursivePropertyAccessor")
val Context.appComponent: AppComponent
    get() = when(this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }