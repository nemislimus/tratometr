package com.nemislimus.tratometr.expenses.ui.fragment.adpter

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.widget.Filter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.nemislimus.tratometr.R
import com.nemislimus.tratometr.expenses.ui.fragment.model.AutoCompleteItem

class AutoCompleteAdapter(
    context: Context,
    private val originalItems: List<AutoCompleteItem>
) : ArrayAdapter<AutoCompleteItem>(context, 0, originalItems) {

    private var filteredItems: List<AutoCompleteItem> = originalItems

    override fun getCount(): Int {
        return filteredItems.size
    }

    override fun getItem(position: Int): AutoCompleteItem {
        return filteredItems[position]
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_autocomplete, parent, false)

        val iconImageView: ImageView = view.findViewById(R.id.icon)
        val textView: TextView = view.findViewById(R.id.text)


        val strAdd = Html.fromHtml(context.resources.getString(R.string.add_category), Html.FROM_HTML_MODE_COMPACT)



        if (item.isAdd) {
            /*val str = Html.fromHtml(item.name, Html.FROM_HTML_MODE_COMPACT)
            val combinedStr = SpannableStringBuilder()
            combinedStr.append(str)
            combinedStr.append(strAdd)
            textView.text = combinedStr*/
            textView.text = item.name + context.resources.getString(R.string.add_category)

            iconImageView.background = null
            iconImageView.imageTintList = null
            iconImageView.setPadding(0)
            iconImageView.setImageResource(R.drawable.add_button)
        } else {
            textView.text = item.name
            iconImageView.setBackgroundResource(R.drawable.circle_icons)
            val tintColor = ContextCompat.getColor(context, R.color.cards)
            iconImageView.imageTintList = ColorStateList.valueOf(tintColor)
            val paddingInDp = 6 // значение в dp
            val scale = context.resources.displayMetrics.density // Получаем плотность экрана
            val paddingInPx = (paddingInDp * scale + 0.5f).toInt() // Конвертируем dp в px
            textView.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)
            if (item.iconResId != 0) {
                iconImageView.setImageResource(item.iconResId)
            } else {
                iconImageView.setImageResource(R.drawable.add_button)
            }
        }

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filterPattern = constraint?.toString()?.lowercase()?.trim() ?: ""

                results.values = if (filterPattern.isEmpty()) {
                    originalItems
                } else {
                    originalItems.filter {
                        it.name.lowercase().contains(filterPattern)
                    }
                }

                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredItems = results?.values as List<AutoCompleteItem>
                notifyDataSetChanged()
            }
        }
    }
}