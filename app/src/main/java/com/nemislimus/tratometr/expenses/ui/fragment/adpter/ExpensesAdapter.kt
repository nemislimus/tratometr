package com.nemislimus.tratometr.expenses.ui.fragment.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nemislimus.tratometr.databinding.ItemHistoryDateBinding
import com.nemislimus.tratometr.databinding.ItemHistoryExpenseBinding
import com.nemislimus.tratometr.expenses.ui.fragment.viewholder.ExpensesDateViewHolder
import com.nemislimus.tratometr.expenses.ui.fragment.viewholder.ExpensesViewHolder
import com.nemislimus.tratometr.expenses.ui.viewmodel.history_model.Historical

class ExpensesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = mutableListOf<Historical>()

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Historical.HistoryDate -> 1
            is Historical.HistoryContent -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> ExpensesDateViewHolder(ItemHistoryDateBinding.inflate(layoutInspector, parent, false))
            2 -> ExpensesViewHolder(ItemHistoryExpenseBinding.inflate(layoutInspector, parent, false))
            else -> error("Не известный viewType = [$viewType]. Не могу создать холдер")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> {
                val dateHolder = holder as ExpensesDateViewHolder
                dateHolder.bind(items[position] as Historical.HistoryDate)
            }
            2 -> {
                val expenseHolder = holder as ExpensesViewHolder
                expenseHolder.bind(items[position] as Historical.HistoryContent)
            }
            else -> error("Не известный viewType = [$holder.itemViewType]. Не могу создать холдер")
        }
    }

    override fun getItemCount(): Int = items.size
}