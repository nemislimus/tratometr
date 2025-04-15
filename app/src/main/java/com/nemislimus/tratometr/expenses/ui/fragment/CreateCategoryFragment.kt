package com.nemislimus.tratometr.expenses.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nemislimus.tratometr.common.appComponent
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentCreateCategoryBinding
import com.nemislimus.tratometr.expenses.ui.fragment.adpter.CreateCategoryAdapter
import com.nemislimus.tratometr.expenses.ui.viewmodel.CreateCategoryViewModel
import javax.inject.Inject

class CreateCategoryFragment : BindingFragment<FragmentCreateCategoryBinding>() {

    @Inject
    lateinit var vmFactory: CreateCategoryViewModel.Factory
    private val viewModel: CreateCategoryViewModel by viewModels { vmFactory }
    private val adapter = CreateCategoryAdapter()


    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateCategoryBinding {
        return FragmentCreateCategoryBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvIconList.adapter = adapter
        adapter.setItems(viewModel.getIconsItems())

    }

    override fun onDestroyFragment() {

    }

}