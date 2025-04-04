package com.nemislimus.tratometr.expense_history.domain.model

import java.math.BigDecimal

data class Expense(
    val id: Long,
    val date: Long,
    val amount: BigDecimal,
    val category: String,
    val iconResId: Int?,
    val note: String?
)
