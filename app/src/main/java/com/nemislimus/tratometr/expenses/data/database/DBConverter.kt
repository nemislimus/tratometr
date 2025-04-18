package com.nemislimus.tratometr.expenses.data.database

import com.nemislimus.tratometr.expenses.data.database.entities.CategoryEntity
import com.nemislimus.tratometr.expenses.data.database.entities.ExpenseEntity
import com.nemislimus.tratometr.expenses.domain.model.Category
import com.nemislimus.tratometr.expenses.domain.model.Expense
import java.math.BigDecimal
import java.math.RoundingMode

class DBConverter {
    fun map(expense: Expense): ExpenseEntity {
        return ExpenseEntity (
            expense.id,
            expense.date,
            map(expense.amount),
            expense.category,
            expense.iconResId,
            expense.description
        )
    }

    fun map(expense: ExpenseEntity): Expense {
        return Expense(
            expense.id,
            expense.date,
            map(expense.amount),
            expense.category,
            expense.iconResId,
            expense.description
        )
    }

    fun map(category: Category): CategoryEntity {
        return CategoryEntity (
            category.name,
            category.iconResId
        )
    }

    fun map(category: CategoryEntity): Category {
        return Category (
            category.name,
            category.iconResId
        )
    }

    fun map(amount: Long): BigDecimal {
        return BigDecimal(amount).divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
    }

    private fun map(amount: BigDecimal): Long {
        return amount.multiply(BigDecimal("100")).toLong()
    }
}