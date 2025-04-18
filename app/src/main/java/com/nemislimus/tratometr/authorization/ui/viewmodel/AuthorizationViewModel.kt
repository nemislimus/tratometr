package com.nemislimus.tratometr.authorization.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nemislimus.tratometr.authorization.domain.AuthInteractor
import com.nemislimus.tratometr.authorization.domain.TokensStorageInteractor
import com.nemislimus.tratometr.authorization.domain.models.Resource
import com.nemislimus.tratometr.authorization.domain.models.Tokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthorizationViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val tokensStorageInteractor: TokensStorageInteractor
) :
    ViewModel() {

    suspend fun authorization(email: String, password: String): Resource<Tokens> = withContext(
        Dispatchers.IO
    ) {
        authInteractor.login(email, password)
    }

    fun putTokensToStorage(tokens: Tokens) {
        tokensStorageInteractor.putTokens(tokens)
    }

    class Factory @Inject constructor(
        private val authInteractor: AuthInteractor,
        private val tokensStorageInteractor: TokensStorageInteractor
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == AuthorizationViewModel::class.java)
            return AuthorizationViewModel(authInteractor, tokensStorageInteractor) as T
        }
    }
}