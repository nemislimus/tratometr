package com.nemislimus.tratometr.expenses.ui.fragment.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.nemislimus.tratometr.R
import com.nemislimus.tratometr.databinding.ItemHistoryDateBinding
import com.nemislimus.tratometr.expenses.ui.viewmodel.history_model.Historical
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpensesDateViewHolder(private val binding: ItemHistoryDateBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Historical.HistoryDate) {
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        val currentDate = Date()
        val today = dateFormat.format(currentDate)
        if (item.date.equals(today)) {
            binding.tvDate.setText(R.string.today)
        } else {
            binding.tvDate.text = item.date
        }
    }
}