package com.nemislimus.tratometr.authorization.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nemislimus.tratometr.authorization.domain.AuthInteractor
import com.nemislimus.tratometr.authorization.domain.TokensStorageInteractor
import com.nemislimus.tratometr.authorization.domain.models.Resource
import com.nemislimus.tratometr.authorization.domain.models.Tokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthorizationViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val tokensStorageInteractor: TokensStorageInteractor
) :
    ViewModel() {

    private val _authState = MutableStateFlow<Resource<Tokens>?>(null)
    val authState: StateFlow<Resource<Tokens>?> = _authState.asStateFlow()

    fun authorize(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = null
            val result = authInteractor.login(email, password)
            _authState.value = result
        }
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