package com.nemislimus.tratometr.expenses.ui.fragment.model

import com.nemislimus.tratometr.expenses.domain.model.Category

sealed interface CategoryListState {

    data class  Content(
        val playlists: List<Category>
    ): CategoryListState

    data object Empty: CategoryListState

}