package com.nemislimus.tratometr.expenses.data.database.entities

import androidx.annotation.DrawableRes

data class ExpenseEntity(
    val id: Long,
    val date: Long,
    val amount: Long,
    val category: String,
    @DrawableRes val iconResId: Int,
    val description: String?
)