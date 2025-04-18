package com.nemislimus.tratometr.expenses.ui.model


sealed interface CategoryListState {

    data class  Content(
        val categoriesItems: List<SelectCategoryItem>
    ): CategoryListState

    data object Empty: CategoryListState

}