package com.nemislimus.tratometr.expenses.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nemislimus.tratometr.expenses.domain.api.ExpenseHistoryInteractor
import com.nemislimus.tratometr.expenses.domain.model.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExpenseHistoryViewModel (
    private val interactor: ExpenseHistoryInteractor
): ViewModel() {

    private var foundExpenseLiveData = MutableLiveData(listOf<Expense>())

    fun getFoundExpenseLiveData(): LiveData<List<Expense>> = foundExpenseLiveData

    fun getExpenseListFilter(startDate: Long?, endDate: Long?, category: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor
                .getExpenseListFilter(startDate, endDate, category)
                .collect { foundExpenseLiveData.postValue(it) }
        }
    }





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