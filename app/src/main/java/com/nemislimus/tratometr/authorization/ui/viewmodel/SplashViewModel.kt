package com.nemislimus.tratometr.authorization.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nemislimus.tratometr.authorization.domain.AuthInteractor
import com.nemislimus.tratometr.authorization.domain.TokensStorageInteractor
import com.nemislimus.tratometr.authorization.domain.models.Resource
import com.nemislimus.tratometr.authorization.domain.models.Tokens
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val tokensStorageInteractor: TokensStorageInteractor
) : ViewModel() {

    private val _isDarkMode = MutableLiveData<Boolean>(false)
    fun isDarkMode(): LiveData<Boolean> = _isDarkMode

    suspend fun checkAccessToken(): Boolean? {
        val accessToken = tokensStorageInteractor.getTokens().accessToken
        return authInteractor.check(accessToken).value == true
    }

    suspend fun refreshTokens(): Resource<Tokens>{
        val refreshToken = tokensStorageInteractor.getTokens().refreshToken ?: ""
        val resource = authInteractor.refresh(refreshToken)
        var freshTokens = Tokens(null, null)
        if (resource is Resource.Success){
            freshTokens = resource.value!!
            putTokensToStorage(freshTokens)
            return resource
        } else {
            return resource
        }
    }

    private fun putTokensToStorage(tokens: Tokens){
        tokensStorageInteractor.putTokens(tokens)
    }

    fun clearTokens(){//Временно тут для тестирования
        tokensStorageInteractor.clearTokens()
    }

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