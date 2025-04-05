package com.nemislimus.tratometr.case.db

import com.nemislimus.tratometr.case.db.entity.CategoryEntity
import com.nemislimus.tratometr.case.db.entity.ExpenseEntity
import com.nemislimus.tratometr.case.db.model.Category
import com.nemislimus.tratometr.case.db.model.Expense
import java.math.BigDecimal

class DbConvertor {
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
}