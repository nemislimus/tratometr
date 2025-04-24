package com.nemislimus.tratometr.common.util

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.nemislimus.tratometr.expenses.domain.model.Expense
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExporterToCSV {

    fun exportExpensesToCSV(context: Context, expenses: List<Expense>) {
        val dateFormat = SimpleDateFormat("yyyy_MM_dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val fileName = "${currentDate}_расходы.csv"

        // Получаем путь для сохранения файла
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        try {
            // Создаем файл и записываем данные
            val fileOutputStream = FileOutputStream(file)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            outputStreamWriter.append("Дата;Категория;Сумма\n")      // Записываем заголовок

            val decimalFormat = DecimalFormat("#,##0.00")
            // Записываем данные о расходах
            for (expense in expenses) {
                val dateFormatted = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(expense.date))
                val amountFormatted = decimalFormat.format(expense.amount)
                outputStreamWriter.append("$dateFormatted;${expense.category};$amountFormatted\n")
            }
            outputStreamWriter.flush()
            outputStreamWriter.close()  // Закрываем поток

            // Показываем Toast с путем к файлу
            Toast.makeText(context, "Файл сохранен: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Ошибка при сохранении файла", Toast.LENGTH_SHORT).show()
        }
    }
}