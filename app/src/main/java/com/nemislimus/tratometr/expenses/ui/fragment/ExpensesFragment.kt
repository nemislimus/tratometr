package com.nemislimus.tratometr.expenses.ui.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nemislimus.tratometr.common.appComponent
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentExpensesBinding
import com.nemislimus.tratometr.expenses.ui.viewmodel.CreateExpenseViewModel
import javax.inject.Inject

class ExpensesFragment : BindingFragment<FragmentExpensesBinding>() {

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
    ): FragmentExpensesBinding {
        return FragmentExpensesBinding.inflate(inflater,container,false)
    }

}