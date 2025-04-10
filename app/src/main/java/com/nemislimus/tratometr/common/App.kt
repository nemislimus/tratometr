package com.nemislimus.tratometr.common

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.nemislimus.tratometr.di.AppComponent
import com.nemislimus.tratometr.di.DaggerAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class App : Application() {

    private val appScope = CoroutineScope(Dispatchers.IO)
    private var isDarkMode = false

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .setContextToComponent(this)
            .build()

        appScope.launch {
            val setDarkModeJob = launch {
                isDarkMode = appComponent.getSettingsRepository().getSettings().isDarkMode
            }
            setDarkModeJob.join()

            switchTheme()
        }
    }

    override fun onTerminate() {
        appScope.cancel()
        super.onTerminate()
    }

    fun switchTheme(darkThemeEnabled: Boolean = isDarkMode) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}

/** Поле расширения для удобного получения доступа к appComponent
 */
@Suppress("RecursivePropertyAccessor")
val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }