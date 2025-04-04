package com.nemislimus.tratometr.expense_history.data.db.dao

import android.content.ContentValues
import com.nemislimus.tratometr.case.db.DBHelper
import com.nemislimus.tratometr.expense_history.data.db.entity.ExpenseEntity
import javax.inject.Inject

class ExpensesDao @Inject constructor(
    private val databaseHelper: DBHelper,
){

    // Выборка строк-расходов за период и по категории
    /*  Образец запроса
        SELECT EXPENSES.[_id], EXPENSES.DATE, EXPENSES.AMOUNT, EXPENSES.CATEGORY, EXPENSES.NOTE, CATEGORIES.ICON_RES_ID
        FROM EXPENSES LEFT JOIN CATEGORIES ON EXPENSES.CATEGORY = CATEGORIES.CATEGORY
        WHERE (EXPENSES.DATE) >= #3/28/2025# And (EXPENSES.DATE) < #3/30/2025# AND (EXPENSES.CATEGORY) = "Еда"
        ORDER BY EXPENSES.DATE;
    */
    fun getExpenses(startDate: Long?, endDate: Long?, category: String?): List<ExpenseEntity> {
        val db = databaseHelper.readableDatabase
        val expenses = mutableListOf<ExpenseEntity>()
        // Начинаем строить базовый запрос
        val queryBuilder = StringBuilder("SELECT EXPENSES.[_id], EXPENSES.DATE, EXPENSES.AMOUNT, EXPENSES.CATEGORY, EXPENSES.NOTE, CATEGORIES.ICON_RES_ID ")
        queryBuilder.append("FROM EXPENSES LEFT JOIN CATEGORIES ON EXPENSES.CATEGORY = CATEGORIES.CATEGORY ")
        queryBuilder.append("WHERE  1=1")
        val args = mutableListOf<String>()
        // Добавляем условия для дат, если они заданы
        if (startDate != null) {
            queryBuilder.append(" AND EXPENSES.DATE >= ?")
            args.add(startDate.toString())
        }
        if (endDate != null) {
            queryBuilder.append(" AND EXPENSES.DATE < ?")
            args.add(endDate.toString())
        }
        // Добавляем условие для категории, если она задана
        if (category != null) {
            queryBuilder.append(" AND EXPENSES.CATEGORY = ?")
            args.add("%$category%")
        }
        // Добавляем сортировку
        queryBuilder.append(" ORDER BY EXPENSES.DATE;")
        // Выполняем запрос
        val cursor = db.rawQuery(queryBuilder.toString(), args.toTypedArray())

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(0)
                val date = cursor.getLong(1)
                val amount = cursor.getLong(2)
                val category = cursor.getString(3)
                val note = cursor.getString(4)
                val iconResId = cursor.getInt(5)
                expenses.add(ExpenseEntity(id, date, amount, category, iconResId, note))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return expenses
    }

    // Добавление новой строки-расхода, возвращает id записи или -1 в случае ошибки
    fun addExpense(expense: ExpenseEntity): Long {
        val db = databaseHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("DATE", expense.date)
            put("AMOUNT", expense.amount)
            put("CATEGORY", expense.category)
            put("NOTE", expense.note)
        }
        val id = db.insert("EXPENSES", null, contentValues)
        db.close()
        return id
    }

    // Обновление строки-расхода
    fun updateExpense(expense: ExpenseEntity) {
        val db = databaseHelper .writableDatabase
        val contentValues = ContentValues().apply {
            put("DATE", expense.date)
            put("AMOUNT", expense.amount)
            put("CATEGORY", expense.category)
            put("NOTE", expense.note)
        }
        val whereArgs = arrayOf(expense.id.toString())
        db.update("EXPENSES", contentValues, "_id = ?", whereArgs)
        db.close()
    }

    // Удаление строки-расхода по id
    fun deleteExpense(expenseId: Long) {
        val db = databaseHelper.writableDatabase
        val whereArgs = arrayOf(expenseId.toString())
        db.delete("EXPENSES", "_id = ?", whereArgs)
        db.close()
    }
}