package com.nemislimus.tratometr.expenses.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nemislimus.tratometr.expenses.domain.api.ExpenseHistoryInteractor
import com.nemislimus.tratometr.expenses.ui.model.CategoryListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectCategoryViewModel(
    private val interactor: ExpenseHistoryInteractor
) : ViewModel() {

    private val state = MutableLiveData<CategoryListState>()
    fun observeState(): LiveData<CategoryListState> = state

    fun getAllCategories() {
        val categories = interactor.getAllCategoriesListWithIcons()
        when {
            categories.isEmpty() -> state.postValue(CategoryListState.Empty)
            else -> state.postValue(CategoryListState.Content(categories))
        }
    }


    class Factory @Inject constructor(
        private val expenseInteractor: ExpenseHistoryInteractor
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == SelectCategoryViewModel::class.java)
            return SelectCategoryViewModel(expenseInteractor) as T
        }
    }

}