package com.nemislimus.tratometr.expense_history.data.db.entity

data class ExpenseEntity(
    val id: Long,
    val date: Long,
    val amount: Long,
    val category: String,
    val iconResId: Int?,
    val note: String?
)
