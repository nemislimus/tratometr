package com.nemislimus.tratometr.analytics.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nemislimus.tratometr.analytics.domain.usecase.GetAllCategoriesFractionsUseCase
import com.nemislimus.tratometr.analytics.ui.model.AnalyticsState
import javax.inject.Inject

class AnalyticsViewModel(
    private val getFractionsUseCase: GetAllCategoriesFractionsUseCase
) : ViewModel() {

    private val state = MutableLiveData<AnalyticsState>()
    fun observeState(): LiveData<AnalyticsState> = state


    class Factory @Inject constructor(
        private val useCase: GetAllCategoriesFractionsUseCase
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == AnalyticsViewModel::class.java)
            return AnalyticsViewModel(useCase) as T
        }
    }
}