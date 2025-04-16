package com.nemislimus.tratometr.expenses.ui.fragment.model

import androidx.annotation.DrawableRes

data class CreateCategoryItem(
    @DrawableRes val iconResId: Int,
    var isSelected: Boolean
)