package com.nemislimus.tratometr.expenses.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.nemislimus.tratometr.R
import com.nemislimus.tratometr.common.appComponent
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentCreateExpenseBinding
import com.nemislimus.tratometr.expenses.domain.model.Category
import com.nemislimus.tratometr.expenses.domain.model.Expense
import com.nemislimus.tratometr.expenses.ui.model.AutoCompleteItem
import com.nemislimus.tratometr.expenses.ui.fragment.adpter.AutoCompleteAdapter
import com.nemislimus.tratometr.expenses.ui.viewmodel.CreateExpenseViewModel
import com.nemislimus.tratometr.expenses.ui.viewmodel.history_model.Historical
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject
import kotlin.random.Random

class CreateExpenseFragment : BindingFragment<FragmentCreateExpenseBinding>() {

    companion object {
        private const val EXTRA_EXPENSE = "EXTRA_EXPENSE"       // Тег для РАСХОДА

        fun createArgs(expense: Historical.HistoryContent? ): Bundle =
            bundleOf(EXTRA_EXPENSE to expense?.expense)
    }

    private var expense: Expense? = null
    private var isAddMode = true                        // Режим true - добавление, false - редактирование
    private lateinit var itemsOriginal: List<AutoCompleteItem>
    private lateinit var items: MutableList<AutoCompleteItem>
    private lateinit var adapter: AutoCompleteAdapter
    private lateinit var typedValue: TypedValue
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private var currentText: String = ""
    private var scale = 0f
    private lateinit var ivIcon: ImageView
    private var dateInMilisecond = 0L

    @Inject
    lateinit var vmFactory: CreateExpenseViewModel.Factory
    private val viewModel: CreateExpenseViewModel by viewModels { vmFactory }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateExpenseBinding {
        return FragmentCreateExpenseBinding.inflate(inflater,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Восстановление состояния
        if (savedInstanceState != null) {
            binding.actvCategory.setText(savedInstanceState.getString("Caregory"))
            binding.etAmount.setText(savedInstanceState.getString("Amount"))
            binding.etDescription.setText(savedInstanceState.getString("Description"))
            dateInMilisecond = savedInstanceState.getLong("Date", System.currentTimeMillis())
            binding.tvDate.text = DateFormat.format("dd.MM.yyyy", dateInMilisecond).toString()
        } else {
            // Получаем расход
            expense = getExpense()

            dateInMilisecond = today()
        }

        isAddMode = expense == null
        // Настройка экрана
        if (isAddMode) showAddMode() else showEditMode()

        autoCompleteTextView = binding.actvCategory
        ivIcon = binding.ivIcon
        scale = requireContext().resources.displayMetrics.density // Получаем плотность экрана
        autoCompleteTextView.dropDownVerticalOffset = 25        // Отступ списка вниз от поля
        typedValue = TypedValue()

        binding.tvTitle.setOnClickListener {
            findNavController().popBackStack()
        }

        items = mutableListOf()

        adapter = AutoCompleteAdapter(requireContext(), items)
        autoCompleteTextView.setAdapter(adapter)
        // Получаем список категорий
        updateItems()


        autoCompleteTextView.threshold = 1 // Начинать показывать предложения после ввода 1 символа

        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = (parent.getItemAtPosition(position) as AutoCompleteItem)
            if (!selectedItem.isAdd) {
                autoCompleteTextView.setText(selectedItem.name)
                showIcons(selectedItem.iconResId)
                // Убираем индикацию ошибки
                errorState(false, binding.actvCategory)
                binding.categoryError!!.isVisible = false
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
                if (itemsOriginal.any { it.name == currentStr}) {
                    if (items[0].isAdd) {
                        items.removeAt(0)
                        val foundItem = itemsOriginal.find { it.name == currentStr }
                        showIcons(foundItem?.iconResId)
                        autoCompleteTextView.dismissDropDown() // Скрываем список
                        // Убираем индикацию ошибки
                        errorState(false, autoCompleteTextView)
                        binding.categoryError!!.isVisible = false
                    }
                } else {
                    showIcons(null)
                    if (items.size > 0 && items[0].isAdd) {
                        items[0].name = currentStr
                    } else {
                        items.add(0, AutoCompleteItem(currentStr, 0, true))
                    }
                }
                adapter.notifyDataSetChanged()

            }
            override fun afterTextChanged(s: Editable?) {
                // Проверка, надо ли сделать кнопку Сохранить активной
                binding.btnAction!!.isEnabled = enableBtnAction()
            }
        })

        binding.tvDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.select_date_expense)
                .build()
            datePicker.addOnPositiveButtonClickListener {
                dateInMilisecond = datePicker.selection!!
                binding.tvDate.text = DateFormat.format("dd.MM.yyyy", dateInMilisecond).toString()
            }
            datePicker.show(requireActivity().supportFragmentManager, "date_picker")
        }

        // Ограничение ввода суммы, не даем ввсести больше двух симоволов после точки
        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    val parts = s.toString().split(".")
                    if (parts.size == 2 && parts[1].length > 2) {                       // если введен третий симовол после точки
                        binding.etAmount.setText(s.substring(0, s.length - 1))          // Отменяем ввод последнего символа
                        binding.etAmount.setSelection(binding.etAmount.text.length)     // Устанавливаем курсор в конец текста
                    }
                }
                if (binding.etAmount.text.isNotEmpty()) {
                    val amount = BigDecimal(binding.etAmount.text.toString()).setScale(
                        2,
                        RoundingMode.HALF_UP
                    )
                    // Если Сумма введена правильно
                    if (amount >= BigDecimal("0.01") && amount <= BigDecimal("999999999999999")) {
                        // Убираем индикацию ошибки
                        errorState(false, binding.etAmount)
                        binding.amountError!!.isVisible = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Проверка, надо ли сделать кнопку Сохранить активной
                binding.btnAction!!.isEnabled = enableBtnAction()
                // Если сумма введена правильно, убираем индикацию ошибки
                val amount = BigDecimal(binding.etAmount.text.toString()).setScale(2, RoundingMode.HALF_UP)
                if (amount >= BigDecimal("0.01") && amount <= BigDecimal("999999999999999")) {
                    errorState(false, binding.etAmount)
                    binding.amountError!!.isVisible = false
                }
            }
        })

        binding.btnAction!!.setOnClickListener {
            if (!errorInCaregoryOrAmount()) {
                executeSave()
            }
        }
    }

    private fun executeSave() {
        val newExpense = Expense(
            expense?.id ?: 0,
            dateInMilisecond,
            BigDecimal(binding.etAmount.text.toString()),
            binding.actvCategory.text.toString(),
            0,
            binding.etDescription.text.toString()
        )
        if (isAddMode) {
            viewModel.addNewExpense(newExpense)
        } else {
            viewModel.updateExpense(newExpense)
        }
        findNavController().popBackStack()
    }

    private fun openCategoryWindow(category: String){
        Toast.makeText(requireContext(), "Переход к окну Создание категории", Toast.LENGTH_SHORT).show()
        // Для отладки создаем категорию ***********************************************************************************
        val icons = listOf(
            R.drawable.ic_custom_cat_01,
            R.drawable.ic_custom_cat_02,
            R.drawable.ic_custom_cat_03,
            R.drawable.ic_custom_cat_04,
            R.drawable.ic_custom_cat_05
        )
        val randomIndex = Random.nextInt(0, 5)
        val newCategory = Category(autoCompleteTextView.text.toString(), icons[randomIndex])
        viewModel.addNewCategory(newCategory) {
            updateItems()
        }
        //********************************************************************************************************************
    }

    private fun showIcons(iconResId: Int?) {
        val leftPadding = if (iconResId != null) 64 else 16
        autoCompleteTextView.setPadding(
            (leftPadding * scale + 0.5f).toInt(),
            (12 * scale + 0.5f).toInt(),
            (16 * scale + 0.5f).toInt(),
            (12 * scale + 0.5f).toInt()
        )

        ivIcon.isVisible = iconResId != null
        if (iconResId != null) {
            ivIcon.setImageDrawable(
                AppCompatResources.getDrawable(requireContext(), iconResId)
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showEditMode() {
        binding.tvTitle.setText(R.string.edit_expense)
        binding.btnAction?.setText(R.string.btn_save)
        binding.actvCategory.setText(expense!!.category)
        binding.tvDate.text = DateFormat.format("dd.MM.yyyy", expense!!.date).toString()
        binding.etAmount.setText(expense!!.amount.toString())
        expense!!.description?.let { binding.etDescription.setText(expense!!.description) }
        // Проверка, надо ли сделать кнопку Сохранить активной
        binding.btnAction!!.isEnabled = enableBtnAction()
    }

    private fun showAddMode() {
        binding.tvTitle.setText(R.string.add_expense)
        binding.btnAction?.setText(R.string.btn_add)
        binding.tvDate.text = DateFormat.format("dd.MM.yyyy", dateInMilisecond).toString()
    }

    private fun errorInCaregoryOrAmount(): Boolean {
        val errCategory = errorInCaregory()
        val errAmount = errorInAmount()
        return (errCategory || errAmount)
    }

    private fun errorInCaregory(): Boolean {
        if (items.any { it.name == autoCompleteTextView.text.toString() }) {
            errorState(false, autoCompleteTextView)
            binding.categoryError!!.isVisible = false
            return false
        } else {
            errorState(true, autoCompleteTextView)
            binding.categoryError!!.isVisible = true
            return true
        }
    }

    private fun errorInAmount(): Boolean {
        val amount = BigDecimal(binding.etAmount.text.toString()).setScale(2, RoundingMode.HALF_UP)
        if (amount >= BigDecimal("0.01") && amount <= BigDecimal("999999999999999")) {
            errorState(false, binding.etAmount)
            binding.amountError!!.isVisible = false
            return false
        } else {
            errorState(true, binding.etAmount)
            binding.amountError!!.isVisible = true
            return true
        }
    }


    private fun enableBtnAction(): Boolean {
        return (binding.actvCategory.text.isNotEmpty() && binding.etAmount.text.isNotEmpty())
    }

    private fun errorState(isError: Boolean, view: EditText) {
        if (isError) {
            requireContext().theme.resolveAttribute(R.attr.appErrorColor, typedValue, true)
            view.setTextColor(typedValue.data)
            view.setBackgroundResource(R.drawable.card_background_error)
        } else {
            view.setBackgroundResource(R.drawable.card_background_regular)
            requireContext().theme.resolveAttribute(R.attr.appTextPrimary, typedValue, true)
            view.setTextColor(typedValue.data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("Caregory", binding.actvCategory.text.toString())
        outState.putLong("Date", dateInMilisecond)
        outState.putString("Amount", binding.etAmount.text.toString())
        outState.putString("Description", binding.etDescription.text.toString())
    }

    private fun updateItems() {
        viewModel.getAllCategories { newItems ->
            itemsOriginal = newItems                // Сохраняем отдельно исходный список
            // Подготовка списка для адаптера
            val sortedItems = newItems.sortedBy { it.name }.toMutableList() // Сортировка списка по алфавиту
            sortedItems.add(0, AutoCompleteItem("", 0, true))
            // Смена списка в адаптере
            items.clear()
            items.addAll(sortedItems)
            adapter.notifyDataSetChanged()
        }
    }

    private fun today(): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun getExpense(): Expense? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(EXTRA_EXPENSE, Expense::class.java)
        } else {
            requireArguments().getSerializable(EXTRA_EXPENSE) as Expense
        }

    }
}