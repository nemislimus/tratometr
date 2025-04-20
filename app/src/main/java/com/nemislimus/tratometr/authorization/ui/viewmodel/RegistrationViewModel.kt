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

class RegistrationViewModel @Inject constructor(
    val authInteractor: AuthInteractor,
    val tokensStorageInteractor: TokensStorageInteractor
) : ViewModel() {

    suspend fun registration(email: String, password: String): Resource<Tokens> = withContext(
        Dispatchers.IO
    ) {
        val resource = authInteractor.register(email, password)
        resource
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
            require(modelClass == RegistrationViewModel::class.java)
            return RegistrationViewModel(authInteractor, tokensStorageInteractor) as T
        }
    }
}