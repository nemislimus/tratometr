package com.nemislimus.tratometr.expenses.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nemislimus.tratometr.expenses.data.database.ExpenseHistoriRepositoryImpl
import com.nemislimus.tratometr.expenses.domain.api.ExpenseHistoriInteractor
import com.nemislimus.tratometr.expenses.domain.model.Category
import com.nemislimus.tratometr.expenses.ui.fragment.model.AutoCompleteItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateExpenseViewModel @Inject constructor(
    private val repository: ExpenseHistoriRepositoryImpl
): ViewModel() {

    // Заменить на параметр когда будет создан в DI *********************************************************************************
    //private val interactor = ExpenseHistoriInteractorImpl(repository)
    // ******************************************************************************************************************************

    fun getAllCategories(callback: (List<AutoCompleteItem>) -> Unit) {
        viewModelScope.launch {
            val categories = getAllCategoriesListWithIcons()
            var items = convertCategoriesToAutoCompleteItems(categories)
            callback(items)
        }
    }

    private suspend fun getAllCategoriesListWithIcons() = withContext(Dispatchers.IO) {
        repository.getAllCategoriesListWithIcons()
    }

    private fun convertCategoriesToAutoCompleteItems(categories: List<Category>): List<AutoCompleteItem> {
        return categories.map { category ->
            AutoCompleteItem(
                name = category.name,
                iconResId = category.iconResId,
                isAdd = false // Устанавливаем isAdd в false для всех элементов
            )
        }
    }
}

