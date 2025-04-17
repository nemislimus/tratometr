package com.nemislimus.tratometr.expenses.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nemislimus.tratometr.common.appComponent
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentSelectCategoryBinding
import com.nemislimus.tratometr.expenses.ui.viewmodel.SelectCategoryViewModel
import javax.inject.Inject

class SelectCategoryFragment : BindingFragment<FragmentSelectCategoryBinding>() {

    @Inject
    lateinit var vmFactory: SelectCategoryViewModel.Factory
    private val viewModel: SelectCategoryViewModel by viewModels { vmFactory }

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
        binding.tbSelectCategory.setOnClickListener { findNavController().navigateUp() }

    }


}