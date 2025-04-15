package com.nemislimus.tratometr.expenses.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nemislimus.tratometr.common.util.CustomCategoryIcons
import com.nemislimus.tratometr.expenses.ui.fragment.model.CreateCategoryItem
import javax.inject.Inject

class CreateCategoryViewModel(

) : ViewModel() {


    fun getIconsItems(): List<CreateCategoryItem> = CustomCategoryIcons.entries.map {
        CreateCategoryItem(
            iconResId = it.resId,
            isSelected = it == CustomCategoryIcons.STAR
        )
    }




    class Factory @Inject constructor() : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == CreateCategoryViewModel::class.java)
            return CreateCategoryViewModel() as T
        }
    }

}