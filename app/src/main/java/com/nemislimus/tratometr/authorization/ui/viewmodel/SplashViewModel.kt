package com.nemislimus.tratometr.authorization.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nemislimus.tratometr.authorization.domain.AuthInteractor
import javax.inject.Inject

class SplashViewModel(
//    private val settingsRepo: SettingsRepository,
    private val authInteractor: AuthInteractor, // пока nullable, чтобы прописать в фрагмент
    private val isDarkMode: () -> Boolean,
) : ViewModel() {

    private val _isDarkMode = MutableLiveData<Boolean>()
    fun isDarkMode(): LiveData<Boolean> = _isDarkMode

    init {
        _isDarkMode.postValue(isDarkMode.invoke())

        // Возможно тут же удобно будет сделать проверку токенов через authRepo
    }


    class Factory @Inject constructor(
//        private val settingsRepo: SettingsRepository,
        private val authInteractor: AuthInteractor,
        private val isDarkMode: () -> Boolean
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == SplashViewModel::class.java)
            return SplashViewModel(authInteractor, isDarkMode) as T
        }
    }

    companion object {
        const val ANIM_START_POINT = 0.0f
        const val ANIM_START_LOOP_POINT = 0.69f
        const val ANIM_END_POINT = 1.0f
    }
}