package com.nemislimus.tratometr.expenses.data.database.entities

import androidx.annotation.DrawableRes

data class CategoryEntity(
    val name: String,
    @DrawableRes val iconResId: Int
)
