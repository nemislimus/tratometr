package com.nemislimus.tratometr.expenses.ui.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.nemislimus.tratometr.R
import com.nemislimus.tratometr.common.appComponent
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.common.util.DateRangeHelper
import com.nemislimus.tratometr.common.util.ExpenseFilter
import com.nemislimus.tratometr.common.util.ExpenseFilterCallback
import com.nemislimus.tratometr.common.util.MoneyСonverter
import com.nemislimus.tratometr.common.util.TimePresetManager
import com.nemislimus.tratometr.databinding.FragmentExpensesBinding
import com.nemislimus.tratometr.expenses.ui.viewmodel.CreateExpenseViewModel
import com.nemislimus.tratometr.expenses.ui.viewmodel.ExpenseHistoryViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

class ExpensesFragment : BindingFragment<FragmentExpensesBinding>(), ExpenseFilterCallback {

    @Inject
    lateinit var vmFactory: ExpenseHistoryViewModel.Factory
    private val viewModel: ExpenseHistoryViewModel by viewModels { vmFactory }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExpensesBinding {
        return FragmentExpensesBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Подписываемся на наблюдение за фильтром
        ExpenseFilter.addExpenseFilterListener(this)

        viewModel.getFoundExpenseLiveData().observe(viewLifecycleOwner) { searchList ->
            // Суммируем все расходы из списка и выводим в поле сумма
            val sum = searchList.sumOf { it.amount }
            binding.tvSum.text = MoneyСonverter.convertBigDecimalToRubleString(requireContext(), sum)


            // Отправляем запрос на переработку
        }

        binding.ivSettings.setOnClickListener {
            findNavController().navigate(
                R.id.action_expensesFragment_to_settingsFragment
            )
        }

        binding.ivDiagram.setOnClickListener {
            findNavController().navigate(
                R.id.action_expensesFragment_to_analyticsFragment
            )
        }

        binding.ivCalendar.setOnClickListener {
            val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(R.string.select_range)
                .build()
            dateRangePicker.addOnPositiveButtonClickListener { selection ->
                ExpenseFilter.setDateInterval(selection.first, selection.second + 24 * 60 * 60 *1000, TimePresetManager.PERIOD)
            }
            dateRangePicker.show(requireActivity().supportFragmentManager, "DATE_RANGE_PICKER")
        }
        binding.tvDay.setOnClickListener {
            val range = DateRangeHelper.getCurrentDay()
            ExpenseFilter.setDateInterval(range.first, range.second, TimePresetManager.DAY)
        }
        binding.tvWeek.setOnClickListener {
            val range = DateRangeHelper.getCurrentWeek()
            ExpenseFilter.setDateInterval(range.first, range.second, TimePresetManager.WEEK)
        }
        binding.tvMonth.setOnClickListener {
            val range = DateRangeHelper.getCurrentMonth()
            ExpenseFilter.setDateInterval(range.first, range.second, TimePresetManager.MONTH)
        }
        binding.tvYear.setOnClickListener {
            val range = DateRangeHelper.getCurrentYear()
            ExpenseFilter.setDateInterval(range.first, range.second, TimePresetManager.YEAR)
        }
        binding.tvCategories.setOnClickListener {
            findNavController().navigate(
                R.id.action_expensesFragment_to_selectCategoryFragment
            )
        }

    }


    override fun onFilterChanged(expenseFilter: ExpenseFilter) {
        // Заполняем поле период
        binding.tvRange.text = DateRangeHelper.convertDatesInRange(
            requireContext(),
            expenseFilter.startDate,
            expenseFilter.endDate
        )
        // Выделяем нужную кнопку фильтра
        accentOnButton(expenseFilter.presetButton)
        // Запрос на получение списка найденых расходов
        binding.progressBar.isVisible = true
        viewModel.getExpenseListFilter(
            expenseFilter.startDate,
            expenseFilter.endDate,
            expenseFilter.category
        )
    }

    private fun accentOnButton(presetButton: TimePresetManager?) {
        // Возвращаем все кнопки в исходное состояние
        changeBtnFilterBackground(binding.ivCalendar, false)
        changeBtnFilterBackground(binding.tvDay, false)
        changeBtnFilterBackground(binding.tvWeek, false)
        changeBtnFilterBackground(binding.tvMonth, false)
        changeBtnFilterBackground(binding.tvYear, false)
        // Выделяем нужную кнопку фильтра
        if (presetButton != null) {
            when (presetButton) {
                TimePresetManager.PERIOD -> changeBtnFilterBackground(binding.ivCalendar, true)
                TimePresetManager.DAY -> changeBtnFilterBackground(binding.tvDay, true)
                TimePresetManager.WEEK -> changeBtnFilterBackground(binding.tvWeek, true)
                TimePresetManager.MONTH -> changeBtnFilterBackground(binding.tvMonth, true)
                TimePresetManager.YEAR -> changeBtnFilterBackground(binding.tvYear, true)
            }
        }

    }

    private fun changeBtnFilterBackground (v: View, isAccent: Boolean) {
        val currentTextColor = binding.tvCategories.currentTextColor
        if (v is TextView) {
            val res = if(isAccent) R.drawable.btn_blue_background_r6 else R.drawable.btn_white_background_r6
            val color = if(isAccent) ContextCompat.getColor(requireContext(), R.color.button_text) else currentTextColor
            v.setBackgroundResource(res)
            v.setTextColor(color)
        } else {
            val color = if (isAccent) {
                ContextCompat.getColor(requireContext(), R.color.accented)
            } else {
                currentTextColor
            }
            binding.ivCalendar.imageTintList = ColorStateList.valueOf(color)
        }

    }





    override fun onDestroyView() {
        super.onDestroyView()
        // Удаляем текущий фрагмент как наблюдателя
        ExpenseFilter.removeExpenseFilterListener(this)
    }


}