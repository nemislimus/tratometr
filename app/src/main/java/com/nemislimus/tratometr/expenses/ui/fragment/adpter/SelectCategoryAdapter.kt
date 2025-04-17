package com.nemislimus.tratometr.expenses.ui.fragment.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.nemislimus.tratometr.databinding.ItemCategoryListBinding
import com.nemislimus.tratometr.expenses.domain.model.Category

class SelectCategoryAdapter(
    private val onCategoryClick: (categoryName: String?) -> Unit,
) : RecyclerView.Adapter<SelectCategoryAdapter.SelectCategoryViewHolder>() {

    private var selectedCategoryPosition = RecyclerView.NO_POSITION
    private var previousSelectedCategoryPosition = RecyclerView.NO_POSITION


    private val categories: MutableList<Category> = mutableListOf()

    fun setCategories(items: List<Category>) {
        categories.clear()
        categories.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = categories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectCategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SelectCategoryViewHolder(
            ItemCategoryListBinding.inflate(layoutInflater, parent, false)) { position ->
            if (position != RecyclerView.NO_POSITION) {
                if (position != previousSelectedCategoryPosition) {
                    onCategoryClick(categories.getOrNull(position)?.name)
                } else {
                    onCategoryClick(null)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: SelectCategoryViewHolder, position: Int) {
        categories.getOrNull(position)?.let { category ->
            if (position == previousSelectedCategoryPosition) {
                holder.bind(category)
            } else {
                holder.bind(category, position == selectedCategoryPosition)
            }
        }
    }


    inner class SelectCategoryViewHolder(
        private val binding: ItemCategoryListBinding,
        private val onCategoryItemClick: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Category, isSelected: Boolean = false) {
            val iconDrawable = ContextCompat.getDrawable(itemView.context, model.iconResId)?.mutate()
            binding.ivCategoryIcon.setImageDrawable(iconDrawable)
            binding.tvCategoryName.text = model.name
            binding.ivCategoryCheckbox.isVisible = isSelected

            itemView.setOnClickListener {
                previousSelectedCategoryPosition = selectedCategoryPosition
                selectedCategoryPosition = bindingAdapterPosition
                onCategoryItemClick(bindingAdapterPosition)
                val samePosition = previousSelectedCategoryPosition == selectedCategoryPosition

                if (!samePosition) {
                    notifyItemChanged(previousSelectedCategoryPosition)
                    notifyItemChanged(selectedCategoryPosition)
                } else {
                    notifyItemChanged(selectedCategoryPosition)
                }
            }
        }
    }

}