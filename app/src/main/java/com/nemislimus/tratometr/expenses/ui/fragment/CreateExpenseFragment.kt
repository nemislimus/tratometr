package com.nemislimus.tratometr.expenses.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentCreateExpenseBinding

class CreateExpenseFragment : BindingFragment<FragmentCreateExpenseBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateExpenseBinding {
        return FragmentCreateExpenseBinding.inflate(inflater,container,false)
    }

}