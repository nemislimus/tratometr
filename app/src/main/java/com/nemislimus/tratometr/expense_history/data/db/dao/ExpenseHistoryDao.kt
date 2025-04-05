package com.nemislimus.tratometr.expense_history.data.db.dao

import com.nemislimus.tratometr.App
import com.nemislimus.tratometr.case.db.DBHelper
import com.nemislimus.tratometr.case.db.entity.ExpenseEntity

class ExpenseHistoryDao (

) {

    // ЗАМЕНИТЬ НА ПАРАМЕТРЫ ПРИ СОЗДАНИИ DI ********************************************
    private val dbh = DBHelper(App.appContext)
    //***********************************************************************************

    // Выборка строк-расходов за период и по категории
    /*  Образец запроса
        SELECT EXPENSES.[_id], EXPENSES.DATE, EXPENSES.AMOUNT, EXPENSES.NOTE, EXPENSES.CATEGORY, CATEGORIES.ICON_RES_ID
        FROM EXPENSES LEFT JOIN CATEGORIES ON EXPENSES.CATEGORY = CATEGORIES.CATEGORY_NAME
        WHERE (((EXPENSES.DATE)>=500 And (EXPENSES.DATE)<1555) AND ((EXPENSES.CATEGORY)="Еда"))
        ORDER BY EXPENSES.DATE;
    */
    fun getExpenseListFilter(startDate: Long?, endDate: Long?, category: String?): List<ExpenseEntity> {
        val db = dbh.readableDatabase
        val expenses = mutableListOf<ExpenseEntity>()
        // Начинаем строить базовый запрос
        val queryBuilder =
            StringBuilder("SELECT EXPENSES.[_id], EXPENSES.DATE, EXPENSES.AMOUNT, EXPENSES.NOTE, EXPENSES.CATEGORY, CATEGORIES.ICON_RES_ID ")
        queryBuilder.append("FROM EXPENSES LEFT JOIN CATEGORIES ON EXPENSES.CATEGORY = CATEGORIES.CATEGORY_NAME ")
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
                val categoryName = cursor.getString(3)
                val note = cursor.getString(4)
                val iconResId = cursor.getInt(5)
                expenses.add(ExpenseEntity(id, date, amount, categoryName, iconResId, note))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return expenses
    }

    // Удаление строки-расхода по id
    fun deleteExpense(expenseId: Long) {
        val db = dbh.writableDatabase
        val whereArgs = arrayOf(expenseId.toString())
        db.delete("EXPENSES", "_id = ?", whereArgs)
        db.close()
    }
}