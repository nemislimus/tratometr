package com.nemislimus.tratometr.authorization.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentRegistrationBinding

class RegistrationFragment : BindingFragment<FragmentRegistrationBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegistrationBinding {
        return FragmentRegistrationBinding.inflate(inflater,container,false)
    }

}