package com.nemislimus.tratometr.analytics.data.db.dao

import com.nemislimus.tratometr.App
import com.nemislimus.tratometr.case.db.DBHelper
import com.nemislimus.tratometr.case.db.entity.CategoryEntity

class AnalyticsDao(

) {

    // ЗАМЕНИТЬ НА ПАРАМЕТРЫ ПРИ СОЗДАНИИ DI ********************************************
    private val dbh = DBHelper(App.appContext)
    //***********************************************************************************

    // Список категорий с иконками и фильтром(период, категория)
    /*  Образец запроса
        SELECT EXPENSES.CATEGORY, CATEGORIES.ICON_RES_ID
        FROM EXPENSES LEFT JOIN CATEGORIES ON EXPENSES.CATEGORY = CATEGORIES.CATEGORY_NAME
        WHERE (((EXPENSES.Date)>=500 And (EXPENSES.Date)<1555) AND ((EXPENSES.CATEGORY)="Еда"))
        ORDER BY EXPENSES.CATEGORY;
    */
    fun getExpenseListFilter(startDate: Long?, endDate: Long?, category: String?): List<CategoryEntity> {
        val db = dbh.readableDatabase
        val categories = mutableListOf<CategoryEntity>()
        // Начинаем строить базовый запрос
        val queryBuilder =
            StringBuilder("SELECT EXPENSES.CATEGORY, CATEGORIES.ICON_RES_ID ")
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
        queryBuilder.append(" ORDER BY EXPENSES.CATEGORY;")
        // Выполняем запрос
        val cursor = db.rawQuery(queryBuilder.toString(), args.toTypedArray())

        if (cursor.moveToFirst()) {
            do {
                val categoryName = cursor.getString(0)
                val iconResId = cursor.getInt(1)
                categories.add(CategoryEntity(categoryName, iconResId))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return categories
    }

    // Список сумм расходов по категории с фильтром (период, категория)
    /*  Образец запроса
        SELECT EXPENSES.AMOUNT
        FROM EXPENSES
        WHERE (((EXPENSES.Date)>=500 And (EXPENSES.Date)<1555) AND ((EXPENSES.CATEGORY)="Еда"));
    */
    fun getExpenseAmountsListByCategoryFilter(startDate: Long?, endDate: Long?, category: String?): List<Long> {
        val db = dbh.readableDatabase
        val amounts = mutableListOf<Long>()
        // Начинаем строить базовый запрос
        val queryBuilder =
            StringBuilder("SELECT EXPENSES.AMOUNT ")
        queryBuilder.append("FROM EXPENSES ")
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
        // Выполняем запрос
        val cursor = db.rawQuery(queryBuilder.toString(), args.toTypedArray())

        if (cursor.moveToFirst()) {
            do {
                val amount = cursor.getLong(0)
                amounts.add(amount)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return amounts
    }
}

