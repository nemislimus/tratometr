package com.nemislimus.tratometr.common.util

import android.content.Context
import com.nemislimus.tratometr.R
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

object MoneyСonverter {
    fun convertBigDecimalToRubleString (context: Context,sum: BigDecimal): String {
        val roundedSum = sum.setScale(0, RoundingMode.HALF_UP)   // Округляем до целого числа
        // Форматируем с разделителями разрядов
        val formattedSum = NumberFormat.getInstance(Locale("ru", "RU")).format(roundedSum)
        return context.getString(R.string.formatted_sum, formattedSum)
    }

}