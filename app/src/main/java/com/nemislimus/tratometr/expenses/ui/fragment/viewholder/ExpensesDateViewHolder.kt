package com.nemislimus.tratometr.expenses.ui.fragment.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.nemislimus.tratometr.databinding.ItemHistoryDateBinding
import com.nemislimus.tratometr.expenses.ui.viewmodel.history_model.Historical

class ExpensesDateViewHolder(private val binding: ItemHistoryDateBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Historical.HistoryDate) {
        binding.tvDate.text = item.date
    }
}