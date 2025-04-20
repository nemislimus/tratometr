package com.nemislimus.tratometr.authorization.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nemislimus.tratometr.authorization.domain.AuthInteractor
import com.nemislimus.tratometr.authorization.domain.models.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PassRecoveryViewModel @Inject constructor(val authInteractor: AuthInteractor): ViewModel() {

    suspend fun recoverPass(email: String): Resource<Boolean> = withContext(Dispatchers.IO){
        authInteractor.recovery(email)
    }

    class Factory @Inject constructor(
        private val authInteractor: AuthInteractor,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == PassRecoveryViewModel::class.java)
            return PassRecoveryViewModel(authInteractor) as T
        }
    }
}