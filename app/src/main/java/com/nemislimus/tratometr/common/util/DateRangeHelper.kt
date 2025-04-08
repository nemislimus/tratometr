package com.nemislimus.tratometr.common.util

import android.text.format.DateFormat
import java.util.Calendar
import java.util.Locale

// Выдает startDate и endDate для кнопок День, Неделя, Месяц, Год
// ВНИМАНИЕ! ВСЕ МЕТОДЫ СТАТИЧЕСКИЕ
class DateRangeHelper {
    companion object {
        // Возвращает строку период типа "01.03 - 25.04.25"
        fun convertDatesInRange(startDate: Long, endDate: Long): String {
            val startDateStr = DateFormat.format("d.MM.yy", startDate).toString()
            val endDateStr = DateFormat.format("d.MM.yy", endDate - 1L).toString()
            if (startDateStr == endDateStr) return endDateStr
            val startDateList = startDateStr.split(".")
            val endDateList = endDateStr.split(".")
            return when {
                startDateList[2] != endDateList[2] -> {
                    "$startDateStr - $endDateStr"
                }

                startDateList[1] != endDateList[1] -> {
                    startDateList[0] + "." + startDateList[1] + " - " + endDate
                }

                else -> {
                    startDateList[0] + " - " + endDate
                }
            }
        }

        // Возвращает временной диапазон <0:00 текущей даты, 0:00 следующей даты
        fun getCurrentDay(): Pair<Long, Long> {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val start = calendar.timeInMillis
            val end = start + 24 * 60 * 60 * 1000
            return Pair(start, end)
        }

        // Возвращает временной диапазон <0:00 понедельника текущей недели, 0:00 понедельника следующей недели
        fun getCurrentWeek(): Pair<Long, Long> {
            // Получение текущей даты
            val calendar = Calendar.getInstance(Locale.getDefault())
            // Установка первого дня недели (например, понедельник)
            calendar.firstDayOfWeek = Calendar.MONDAY
            // Получение начала недели
            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val start = calendar.timeInMillis
            // Получение конца недели
            val end = start + 7 * 24 * 60 * 60 * 1000
            return Pair(start, end)
        }

        // Возвращает временной диапазон <0:00 первого дня текущего месяца, 0:00 первого дня следующего месяца
        fun getCurrentMonth(): Pair<Long, Long> {
            // Получение текущей даты
            val calendar = Calendar.getInstance(Locale.getDefault())
            // Установка первого дня месяца
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val start = calendar.timeInMillis
            // Установка последнего дня месяца
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            val end = calendar.timeInMillis + 24 * 60 * 60 * 1000
            return Pair(start, end)
        }

        // Возвращает временной диапазон <0:00 первого дня текущего года, 0:00 первого дня следующего года
        fun getCurrentYear(): Pair<Long, Long> {
            // Получение текущей даты
            val calendar = Calendar.getInstance(Locale.getDefault())
            // Устанавливаем начало года
            calendar.set(Calendar.MONTH, Calendar.JANUARY)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val start = calendar.timeInMillis
            // Устанавливаем конец года
            calendar.set(Calendar.MONTH, Calendar.DECEMBER)
            calendar.set(Calendar.DAY_OF_MONTH, 31)
            calendar.set(Calendar.HOUR_OF_DAY, 24)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val end = calendar.timeInMillis
            return Pair(start, end)
        }
    }
}