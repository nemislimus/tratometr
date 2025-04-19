package com.nemislimus.tratometr.analytics.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nemislimus.tratometr.analytics.ui.model.AnalyticsState
import com.nemislimus.tratometr.expenses.domain.api.ExpenseHistoryInteractor
import javax.inject.Inject

class AnalyticsViewModel(
    private val interactor: ExpenseHistoryInteractor
) : ViewModel() {

    private val state = MutableLiveData<AnalyticsState>()
    fun observeState(): LiveData<AnalyticsState> = state


    class Factory @Inject constructor(
        private val expenseInteractor: ExpenseHistoryInteractor
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == AnalyticsViewModel::class.java)
            return AnalyticsViewModel(expenseInteractor) as T
        }
    }
}