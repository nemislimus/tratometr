package com.nemislimus.tratometr.expenses.ui.fragment.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.nemislimus.tratometr.common.util.MoneyConverter
import com.nemislimus.tratometr.databinding.ItemHistoryExpenseBinding
import com.nemislimus.tratometr.expenses.ui.viewmodel.history_model.Historical

class ExpensesViewHolder(private val binding: ItemHistoryExpenseBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Historical.HistoryContent) {
        val btnEdit = binding.flBackground.ivEdit
        val btnDel = binding.flBackground.ivDel
        val flBackground = binding.flBackground.flBackground
        val flForeground = binding.flForeground.flForeground
        with(binding.flForeground) {
            icon.setImageResource(item.expense.iconResId)
            tvCategory.text = item.expense.category
            tvDescription.text = item.expense.description
            tvAmount.text = MoneyConverter.convertBigDecimalToRubleString(binding.root.context, item.expense.amount)
        }
    }
}