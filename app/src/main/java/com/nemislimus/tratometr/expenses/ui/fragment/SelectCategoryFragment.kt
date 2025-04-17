package com.nemislimus.tratometr.expenses.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nemislimus.tratometr.R
import com.nemislimus.tratometr.common.appComponent
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.common.util.ExpenseFilter
import com.nemislimus.tratometr.common.util.ExpenseFilterCallback
import com.nemislimus.tratometr.databinding.FragmentSelectCategoryBinding
import com.nemislimus.tratometr.expenses.domain.model.Category
import com.nemislimus.tratometr.expenses.ui.fragment.adpter.SelectCategoryAdapter
import com.nemislimus.tratometr.expenses.ui.model.CategoryListState
import com.nemislimus.tratometr.expenses.ui.viewmodel.SelectCategoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectCategoryFragment : BindingFragment<FragmentSelectCategoryBinding>() {

    @Inject
    lateinit var vmFactory: SelectCategoryViewModel.Factory
    private val viewModel: SelectCategoryViewModel by viewModels { vmFactory }
    private var searchTextWatcher: TextWatcher? = null

    private var selectedCategoryName: String? = null

    private val adapter = SelectCategoryAdapter { setSelectedCategory(it) }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelectCategoryBinding {
        return FragmentSelectCategoryBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUiConfigurations()

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            stateProcessing(state)
        }

    }

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
           viewModel.getAllCategories()
        }
    }

    override fun onDestroyFragment() {
        binding.etSearchCategory.removeTextChangedListener(searchTextWatcher)
        searchTextWatcher = null
    }

    private fun setUiConfigurations() {
        binding.rvCategoryList.adapter = adapter

        binding.tbSelectCategory.setOnClickListener { findNavController().navigateUp() }

        binding.btnNewCategory.setOnClickListener {
            findNavController().navigate(
                R.id.action_selectCategoryFragment_to_createCategoryFragment
            )
        }

        binding.btnCategoryApply.setOnClickListener {
            ExpenseFilter.category = selectedCategoryName
            findNavController().navigateUp()
        }

        installTextWatcher()
    }

    private fun installTextWatcher() {
        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable?) {
                adapter.filterCategoriesByName(s.toString())
            }
        }
        binding.etSearchCategory.addTextChangedListener(searchTextWatcher)
    }

    private fun stateProcessing(state: CategoryListState) {
        when(state) {
            is CategoryListState.Content -> showContent(state.playlists)
            CategoryListState.Empty -> showPlaceholder()
        }
    }

    private fun showContent(playlists: List<Category>) {
        adapter.setCategories(playlists)
        showPlaceholder(false)
    }

    private fun showPlaceholder(placeholderVisible: Boolean = true) {
        binding.rvCategoryList.isVisible = !placeholderVisible
        binding.grPlaceholderCategoryList.isVisible = placeholderVisible
    }

    private fun setSelectedCategory(name: String?) {
        selectedCategoryName = name
    }

}