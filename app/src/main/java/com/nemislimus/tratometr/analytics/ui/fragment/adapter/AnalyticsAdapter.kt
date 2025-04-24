package com.nemislimus.tratometr.analytics.ui.fragment.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nemislimus.tratometr.R
import com.nemislimus.tratometr.analytics.domain.model.CategoryFraction
import com.nemislimus.tratometr.common.util.MoneyConverter
import com.nemislimus.tratometr.common.util.RingChartColorsResources
import com.nemislimus.tratometr.databinding.ItemAnalyticsCategoryListBinding

class AnalyticsAdapter : RecyclerView.Adapter<AnalyticsAdapter.AnalyticsViewHolder>() {

    private val fractionsOrigin: ArrayList<CategoryFraction> = arrayListOf()
    private var sortByDescending = true

    fun setFractions(items: List<CategoryFraction>, byDesc: Boolean) {
        fractionsOrigin.clear()
        fractionsOrigin.addAll(items)
        sortByDescending = byDesc
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalyticsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AnalyticsViewHolder(ItemAnalyticsCategoryListBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: AnalyticsViewHolder, position: Int) {
        fractionsOrigin.getOrNull(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = fractionsOrigin.size

    override fun getItemViewType(position: Int): Int {
        return if (sortByDescending) {
            if (position <= NUMB_OF_COLORS - 1) FRACTION else OTHER_FRACTION
        } else {
            if (fractionsOrigin.size <= NUMB_OF_COLORS) {
                FRACTION
            } else {
                if (position < fractionsOrigin.size - NUMB_OF_COLORS) OTHER_FRACTION else FRACTION
            }
        }
    }


    inner class AnalyticsViewHolder(
        private val binding: ItemAnalyticsCategoryListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CategoryFraction) {
            val elementPosition = bindingAdapterPosition
            val iconDrawable: Drawable?
            val percentText: String
            val money: String
            val name: String
            val iconBackColor: Int

            val otherCategoryFractionPosition = if (sortByDescending) {
                NUMB_OF_COLORS - 1
            } else {
                fractionsOrigin.size - NUMB_OF_COLORS
            }

            if (elementPosition == otherCategoryFractionPosition) {
                iconDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_main_cat_other)
                percentText = "${model.fractionPercentValue}%"
                money = MoneyConverter.convertBigDecimalToRublesString(itemView.context, model.categorySumAmount)
                name = ContextCompat.getString(itemView.context, R.string.category_other)
                iconBackColor = ContextCompat.getColor(itemView.context, RingChartColorsResources.COLOR_OTHERS.resId)
            } else {
                iconDrawable = ContextCompat.getDrawable(itemView.context, model.iconResId)
                percentText = "${model.fractionPercentValue}%"
                money = MoneyConverter.convertBigDecimalToRublesString(itemView.context, model.categorySumAmount)
                name = model.name
                iconBackColor = ContextCompat.getColor(itemView.context, RingChartColorsResources.entries[elementPosition].resId)
            }

            binding.ivCategoryIconAnalytic.setImageDrawable(iconDrawable)
            binding.ivCategoryIconBackAnalytic.setColorFilter(iconBackColor)
            binding.tvCategoryNameAnalytic.text = name
            binding.tvPercentAnalytic.text = percentText
            binding.tvAmountAnalytics.text = money
        }
    }

    companion object {
        const val NUMB_OF_COLORS = 8
        private const val FRACTION = 0
        private const val OTHER_FRACTION = 1
    }
}