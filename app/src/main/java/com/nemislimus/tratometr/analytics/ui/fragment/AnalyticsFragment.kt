package com.nemislimus.tratometr.analytics.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nemislimus.tratometr.analytics.ui.viewmodel.AnalyticsViewModel
import com.nemislimus.tratometr.common.appComponent
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentAnalyticsBinding
import javax.inject.Inject

class AnalyticsFragment : BindingFragment<FragmentAnalyticsBinding>() {

    @Inject
    lateinit var vmFactory: AnalyticsViewModel.Factory
    private val viewModel: AnalyticsViewModel by viewModels { vmFactory }


    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAnalyticsBinding {
        return FragmentAnalyticsBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}