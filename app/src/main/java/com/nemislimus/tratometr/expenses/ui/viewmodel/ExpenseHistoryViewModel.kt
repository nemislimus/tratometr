package com.nemislimus.tratometr.expenses.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nemislimus.tratometr.expenses.domain.api.ExpenseHistoryInteractor
import javax.inject.Inject

class ExpenseHistoryViewModel (
    private val interactor: ExpenseHistoryInteractor
): ViewModel() {

    class Factory @Inject constructor(
        private val interactor: ExpenseHistoryInteractor
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == ExpenseHistoryViewModel::class.java)
            return ExpenseHistoryViewModel(interactor) as T
        }
    }

}