package com.nemislimus.tratometr.analytics.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nemislimus.tratometr.analytics.domain.usecase.GetAllCategoriesFractionsUseCase
import com.nemislimus.tratometr.analytics.ui.model.AnalyticsState
import com.nemislimus.tratometr.common.util.ExpenseFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AnalyticsViewModel(
    private val getFractionsUseCase: GetAllCategoriesFractionsUseCase
) : ViewModel() {

//    init {
//        viewModelScope.launch(Dispatchers.Default) {
//            getFractionsByFilter(ExpenseFilter.startDate, ExpenseFilter.endDate)
//        }
//    }

    private val state = MutableLiveData<AnalyticsState>()
    fun observeState(): LiveData<AnalyticsState> = state

    suspend fun getFractionsByFilter(startDate: Long?, endDate: Long?) {
        setState(AnalyticsState.Loading)
        val fractionsFromDatabase = getFractionsUseCase.execute(startDate, endDate)
        if (fractionsFromDatabase.isNotEmpty()){
            setState(AnalyticsState.Content(fractionsFromDatabase))
        } else {
            setState(AnalyticsState.Empty)
        }
    }

    private fun setState(newState: AnalyticsState) {
        state.postValue(newState)
    }

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