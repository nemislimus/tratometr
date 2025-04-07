package com.nemislimus.tratometr.analytics.data.db.dao

import com.nemislimus.tratometr.analytics.domain.api.AnalyticsRepository
import com.nemislimus.tratometr.expenses.data.database.DBConverter
import com.nemislimus.tratometr.expenses.data.database.entities.CategoryEntity
import com.nemislimus.tratometr.expenses.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.math.BigDecimal
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(
    private val analyticsDao: AnalyticsDao,
    private val dbConverter: DBConverter
): AnalyticsRepository {
    // Список категорий с иконками и фильтром(период, категория)
    override fun getCategoriesListWithIconsFiltr(startDate: Long?, endDate: Long?, category: String?): Flow<List<Category>> = flow {
        val expenses = analyticsDao.getCategoriesListWithIconsFiltr(startDate, endDate, category)
        emit(convertFromExpenseEntity(expenses))
    }

    private fun convertFromExpenseEntity(categories: List<CategoryEntity>): List<Category> {
        return categories.map { category -> dbConverter.map(category) }
    }

    // Список сумм расходов по категории с фильтром (период, категория)
    override fun getExpenseAmountsListByCategoryFilter(startDate: Long?, endDate: Long?, category: String?): Flow<List<BigDecimal>> = flow {
        val amounts = analyticsDao.getExpenseAmountsListByCategoryFilter(startDate, endDate, category)
        emit(convertFromLongToBigDecimal(amounts))
    }

    private fun convertFromLongToBigDecimal(amounts: List<Long>): List<BigDecimal> {
        return amounts.map { amount -> dbConverter.map(amount) }
    }
}