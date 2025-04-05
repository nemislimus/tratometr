package com.nemislimus.tratometr.authorization.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentAuthorizationBinding

class AuthorizationFragment : BindingFragment<FragmentAuthorizationBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAuthorizationBinding {
        return FragmentAuthorizationBinding.inflate(inflater,container,false)
    }

}