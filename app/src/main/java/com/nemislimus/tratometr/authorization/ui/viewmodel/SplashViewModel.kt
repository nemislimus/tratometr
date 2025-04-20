package com.nemislimus.tratometr.authorization.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nemislimus.tratometr.authorization.domain.AuthInteractor
import com.nemislimus.tratometr.authorization.domain.TokensStorageInteractor
import com.nemislimus.tratometr.authorization.domain.models.Resource
import com.nemislimus.tratometr.authorization.domain.models.Tokens
import com.nemislimus.tratometr.settings.domain.GetDarkModeValueUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

class SplashViewModel (
    private val authInteractor: AuthInteractor,
    private val tokensStorageInteractor: TokensStorageInteractor,
    private val getDarkModeValueUseCase: GetDarkModeValueUseCase,
) : ViewModel() {

    private val _isDarkMode = MutableLiveData<Boolean>()
    fun isDarkMode(): LiveData<Boolean> = _isDarkMode

    suspend fun checkAccessToken(): Boolean? = withContext(Dispatchers.IO) {
        val accessToken = tokensStorageInteractor.getTokens().accessToken
        authInteractor.check(accessToken).value == true
    }

    suspend fun refreshTokens(): Resource<Tokens> = withContext(Dispatchers.IO){
        val refreshToken = tokensStorageInteractor.getTokens().refreshToken ?: ""
        val resource = authInteractor.refresh(refreshToken)
        var freshTokens = Tokens(null, null)
        if (resource is Resource.Success){
            freshTokens = resource.value!!
            putTokensToStorage(freshTokens)
            resource
        } else {
            resource
        }
    }

    private fun putTokensToStorage(tokens: Tokens){
        tokensStorageInteractor.putTokens(tokens)
    }

    fun clearTokens(){//Временно тут для тестирования
        tokensStorageInteractor.clearTokens()
    }

    fun checkDarkMode() {
        viewModelScope.launch(Dispatchers.IO) {
            _isDarkMode.postValue(getDarkModeValueUseCase.execute())
        }
    }

    class Factory @Inject constructor(
        private val authInteractor: AuthInteractor,
        private val tokensStorageInteractor: TokensStorageInteractor,
        private val getDarkModeValueUseCase: GetDarkModeValueUseCase,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == SplashViewModel::class.java)
            return SplashViewModel(
                authInteractor,
                tokensStorageInteractor,
                getDarkModeValueUseCase
            ) as T
        }
    }

    companion object {
        const val ANIM_START_POINT = 0.0f
        const val ANIM_START_LOOP_POINT = 0.69f
        const val ANIM_END_POINT = 1.0f
    }
}