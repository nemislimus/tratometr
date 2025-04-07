package com.nemislimus.tratometr.analytics.domain.api

import com.nemislimus.tratometr.expenses.domain.model.Category
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface AnalyticsRepository {
    // Список категорий с иконками и фильтром(период, категория)
    fun getCategoriesListWithIconsFiltr(startDate: Long?, endDate: Long?, category: String?): Flow<List<Category>>

    // Список сумм расходов по категории с фильтром (период, категория)
    fun getExpenseAmountsListByCategoryFilter(startDate: Long?, endDate: Long?, category: String?): Flow<List<BigDecimal>>
}