package com.nemislimus.tratometr.authorization.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentPassrecoveryBinding

class PassRecoveryFragment : BindingFragment<FragmentPassrecoveryBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPassrecoveryBinding {
        return FragmentPassrecoveryBinding.inflate(inflater,container,false)
    }

}