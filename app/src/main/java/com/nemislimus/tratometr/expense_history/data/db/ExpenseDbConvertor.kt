package com.nemislimus.tratometr.expense_history.data.db

import com.nemislimus.tratometr.expense_history.data.db.entity.ExpenseEntity
import com.nemislimus.tratometr.expense_history.domain.model.Expense
import java.math.BigDecimal

class ExpenseDbConvertor {
    fun map(expense: Expense): ExpenseEntity {
        return ExpenseEntity (
            expense.id,
            expense.date,
            expense.amount.multiply(BigDecimal("100")).toLong(),
            expense.category,
            expense.iconResId,
            expense.note
        )
    }

    fun map(expense: ExpenseEntity): Expense {
        return Expense(
            expense.id,
            expense.date,
            BigDecimal.valueOf(expense.amount / 100),
            expense.category,
            expense.iconResId,
            expense.note
        )
    }
}