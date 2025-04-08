package com.nemislimus.tratometr.expenses.data.database

import com.nemislimus.tratometr.expenses.data.database.entities.CategoryEntity
import com.nemislimus.tratometr.expenses.data.database.entities.ExpenseEntity
import com.nemislimus.tratometr.expenses.domain.api.ExpenseHistoriRepository
import com.nemislimus.tratometr.expenses.domain.model.Category
import com.nemislimus.tratometr.expenses.domain.model.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExpenseHistoriRepositoryImpl @Inject constructor(
    private val expenseHistoryDao: ExpenseHistoryDao,
    private val dbConverter: DBConverter
): ExpenseHistoriRepository {

    // ################   ЗАПРОСЫ ДЛЯ ОКНА ИСТОРИЯ РАСХОДОВ   #########################################################################################
    // Выборка строк-расходов за период и по категории
    override fun getExpenseListFilter(startDate: Long?, endDate: Long?, category: String?): Flow<List<Expense>> = flow{
        val expenses = expenseHistoryDao.getExpenseListFilter(startDate, endDate, category)
        emit(convertFromExpenseEntity(expenses))
    }

    private fun convertFromExpenseEntity(expenses: List<ExpenseEntity>): List<Expense> {
        return expenses.map { expense -> dbConverter.map(expense) }
    }
    // Удаление строки-расхода по id
    override fun deleteExpense(expenseId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            expenseHistoryDao.deleteExpense(expenseId)
        }
    }

    // ################   ЗАПРОСЫ ДЛЯ ОКНА ДОБАВЛЕНИЕ/РЕДАКТИРОВАНИЕ РАСХОДА   ######################################################################
    //Список всех категорий с иконками (по алфавиту)
    override fun getAllCategoriesListWithIcons(): Flow<List<Category>> = flow {
        val categories = expenseHistoryDao.getAllCategoriesListWithIcons()
        emit(convertFromCategoriesEntity(categories))
    }

    private fun convertFromCategoriesEntity(categories: List<CategoryEntity>): List<Category> {
        return categories.map { category -> dbConverter.map(category) }
    }
    // Добавление нового расхода
    override fun addNewExpense(expense: Expense) {
        CoroutineScope(Dispatchers.IO).launch {
            expenseHistoryDao.addNewExpense(dbConverter.map(expense))
        }
    }
    // Обновление расхода
    override fun updateExpense(expense: Expense) {
        CoroutineScope(Dispatchers.IO).launch {
            expenseHistoryDao.updateExpense(dbConverter.map(expense))
        }
    }

    // ################   ЗАПРОСЫ ДЛЯ ОКНА ВЫБОР КАТЕГОРИИ   ########################################################################################
    // Список категорий с иконками и фильтром(период)
    override fun getCategoriesListWithIconsRange(startDate: Long?, endDate: Long?): Flow<List<Category>> = flow {
        val categories = expenseHistoryDao.getCategoriesListWithIconsRange(startDate, endDate)
        emit(convertFromCategoriesEntity(categories))
    }

    // ################   ЗАПРОСЫ ДЛЯ ОКНА СОЗДАНИЕ КАТЕГОРИИ   #####################################################################################
    // Список всех категорий
    override fun getAllCategoriesList(): Flow<List<String>> = flow {
        val categories = expenseHistoryDao.getAllCategoriesList()
        emit(categories)
    }
    // Добавление новой категорий
    override fun addNewCategory(category: Category) {
        CoroutineScope(Dispatchers.IO).launch {
            expenseHistoryDao.addNewCategory(dbConverter.map(category))
        }
    }

}