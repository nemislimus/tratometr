package com.nemislimus.tratometr.expenses.ui.fragment.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.nemislimus.tratometr.common.util.MoneyConverter
import com.nemislimus.tratometr.databinding.ItemHistoryExpenseBinding
import com.nemislimus.tratometr.expenses.ui.viewmodel.history_model.Historical

class ExpensesViewHolder(private val binding: ItemHistoryExpenseBinding) : RecyclerView.ViewHolder(binding.root) {
    val btnEdit = binding.flBackground.ivEdit
    val btnDel = binding.flBackground.ivDel
    val flBackground = binding.flBackground.flBackground
    val flForeground = binding.flForeground.flForeground
    val tvCategory = binding.flForeground.tvCategory
    val tvDescription = binding.flForeground.tvDescription

    lateinit var item: Historical.HistoryContent

    fun bind(expenseItem: Historical.HistoryContent) {
        item = expenseItem
        tvCategory.text = expenseItem.expense.category
        tvDescription.text = expenseItem.expense.description
        with(binding.flForeground) {
            icon.setImageResource(expenseItem.expense.iconResId)
            tvAmount.text = MoneyConverter.convertBigDecimalToRubleString(binding.root.context, expenseItem.expense.amount)
        }
    }
}