package com.nemislimus.tratometr.authorization.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nemislimus.tratometr.authorization.domain.AuthInteractor
import com.nemislimus.tratometr.authorization.domain.TokensStorageInteractor
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val tokensStorageInteractor: TokensStorageInteractor
) : ViewModel() {

    private val _isDarkMode = MutableLiveData<Boolean>(false)
    fun isDarkMode(): LiveData<Boolean> = _isDarkMode

    class Factory @Inject constructor(
        private val authInteractor: AuthInteractor,
        private val tokensStorageInteractor: TokensStorageInteractor
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == SplashViewModel::class.java)
            return SplashViewModel(authInteractor, tokensStorageInteractor) as T
        }
    }

    companion object {
        const val ANIM_START_POINT = 0.0f
        const val ANIM_START_LOOP_POINT = 0.69f
        const val ANIM_END_POINT = 1.0f
    }
}