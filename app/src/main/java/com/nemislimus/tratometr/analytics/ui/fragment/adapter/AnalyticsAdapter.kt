package com.nemislimus.tratometr.analytics.ui.fragment.adapter

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nemislimus.tratometr.analytics.domain.model.CategoryFraction
import com.nemislimus.tratometr.databinding.ItemAnalyticsCategoryListBinding

class AnalyticsAdapter : RecyclerView.Adapter<AnalyticsAdapter.AnalyticsViewHolder>() {

    private val fractions: MutableList<CategoryFraction> = mutableListOf()
    private val sortedFractions: MutableList<CategoryFraction> = mutableListOf()

    fun setFractions(items: List<CategoryFraction>) {
        fractions.clear()
        fractions.addAll(items)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalyticsViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: AnalyticsViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class AnalyticsViewHolder(
        private val binding: ItemAnalyticsCategoryListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CategoryFraction) {
            val iconDrawable = ContextCompat.getDrawable(itemView.context, model.iconResId)
            binding.ivCategoryIconAnalytic.setImageDrawable(iconDrawable)
            binding.tvCategoryNameAnalytic.text = model.name

        }

    }
}