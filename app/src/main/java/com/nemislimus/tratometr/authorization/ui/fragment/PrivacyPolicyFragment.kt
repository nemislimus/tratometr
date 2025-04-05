package com.nemislimus.tratometr.authorization.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentPrivacyPolicyBinding

class PrivacyPolicyFragment : BindingFragment<FragmentPrivacyPolicyBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPrivacyPolicyBinding {
        return FragmentPrivacyPolicyBinding.inflate(inflater,container,false)
    }

}