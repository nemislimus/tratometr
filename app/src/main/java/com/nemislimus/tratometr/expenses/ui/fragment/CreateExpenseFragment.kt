package com.nemislimus.tratometr.expenses.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.nemislimus.tratometr.common.MainActivity
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentCreateExpenseBinding
import com.nemislimus.tratometr.expenses.ui.fragment.model.AutoCompleteItem
import com.nemislimus.tratometr.expenses.ui.fragment.adpter.AutoCompleteAdapter

class CreateExpenseFragment : BindingFragment<FragmentCreateExpenseBinding>() {
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private var currentText: String = ""

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateExpenseBinding {
        return FragmentCreateExpenseBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        autoCompleteTextView = binding.actvCategory
        val ivIcon = binding.ivIcon
        autoCompleteTextView.setDropDownVerticalOffset(50)

        // Список вариантов для автозаполнения
        val items = (requireActivity() as MainActivity).items2
        val sortedItems = items.sortedBy { it.name }.toMutableList() // Сортировка списка по алфавиту
        sortedItems.add(0, AutoCompleteItem("", 0, true))


        // Создание адаптера
        val adapter = AutoCompleteAdapter(requireContext(), sortedItems)
        autoCompleteTextView.setAdapter(adapter)

        // Настройка минимального количества символов для отображения предложений
        autoCompleteTextView.threshold = 1 // Начинать показывать предложения после ввода 1 символа

        autoCompleteTextView.setOnItemClickListener { parent, _, position, id ->
            val selectedItem = (parent.getItemAtPosition(position) as AutoCompleteItem)
            if (!selectedItem.isAdd) {
                autoCompleteTextView.setText(selectedItem.name)
                ivIcon.setImageDrawable(AppCompatResources.getDrawable(requireContext(), selectedItem.iconResId))
            } else {
                autoCompleteTextView.setText(currentText)
                openCategoryWindow(autoCompleteTextView.text.toString())
            }
        }


        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                currentText = s.toString()
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentStr = s.toString()
                if (items.any { it.name == currentStr}) {
                    if (sortedItems[0].isAdd) {
                        sortedItems.removeAt(0)
                    }
                } else {
                    if (sortedItems[0].isAdd) {
                        sortedItems[0].name = currentStr
                    } else {
                        sortedItems.add(0, AutoCompleteItem(currentStr, 0, true))
                    }
                }
                adapter.notifyDataSetChanged()
                Log.e("МОЁ", sortedItems.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun openCategoryWindow(category: String){
        Toast.makeText(requireContext(), "Переход к окну Создание категории", Toast.LENGTH_SHORT).show()
    }


}