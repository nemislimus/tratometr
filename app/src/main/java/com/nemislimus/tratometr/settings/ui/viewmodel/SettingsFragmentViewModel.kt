package com.nemislimus.tratometr.settings.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nemislimus.tratometr.settings.domain.api.SettingsRepository
import com.nemislimus.tratometr.settings.domain.model.SettingsParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsFragmentViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    private val settingsParams = MutableLiveData<SettingsParams>()
    fun observeSettingsParams(): LiveData<SettingsParams> = settingsParams

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getSettingsParams()
        }
    }

    suspend fun updateSettings(params: SettingsParams) {
        repository.updateSettings(params)
    }

    suspend fun getSettingsParams() {
        settingsParams.postValue(repository.getSettings())
    }

    class Factory @Inject constructor(
        private val repository: SettingsRepository,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == SettingsFragmentViewModel::class.java)
            return SettingsFragmentViewModel(repository) as T
        }
    }

}