package com.nemislimus.tratometr.analytics.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nemislimus.tratometr.analytics.ui.viewmodel.AnalyticsViewModel
import com.nemislimus.tratometr.common.appComponent
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.nemislimus.tratometr.R
import com.nemislimus.tratometr.analytics.domain.model.CategoryFraction
import com.nemislimus.tratometr.analytics.ui.model.AnalyticsState
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentAnalyticsBinding
import javax.inject.Inject
import java.math.BigDecimal

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

        setUiConfigurations()


        val context = requireContext()
        val sumList = mutableListOf<BigDecimal>(BigDecimal("500"), BigDecimal("300"), BigDecimal("100"), BigDecimal("50")) // Значения для диаграммы
        val colorsList = mutableListOf<Int>(
            ContextCompat.getColor(context, R.color.chart_color_1),
            ContextCompat.getColor(context, R.color.chart_color_2),
            ContextCompat.getColor(context, R.color.chart_color_3),
            ContextCompat.getColor(context, R.color.chart_color_4),
        )

        binding.rcvMainChart.setData(colorsList, sumList)
    }

    override fun onStart() {
        super.onStart()
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            stateProcessing(state)
        }
    }

    private fun setUiConfigurations() {
        binding.btnSettings.setOnClickListener {
            findNavController().navigate(
                R.id.action_analyticsFragment_to_settingsFragment
            )
        }

        binding.btnHistory.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun stateProcessing(state: AnalyticsState) {
        when(state) {
            is AnalyticsState.Content -> showContent(state.fractions)
            AnalyticsState.Empty -> showPlaceholder()
            AnalyticsState.Loading -> showLoading()
        }
    }

    private fun showContent(fractions: List<CategoryFraction>) {

    }

    private fun showPlaceholder() {
        binding.grAnalyticsContent.isVisible = false
        binding.pbAnalyticsProgressBar.isVisible = false
        binding.grPlaceholderAnalytics.isVisible = true

        //Обработать отображение диаграммы и суммы
    }

    private fun showLoading() {
        binding.grAnalyticsContent.isVisible = false
        binding.grPlaceholderAnalytics.isVisible = false
        binding.pbAnalyticsProgressBar.isVisible = true

        //Обработать отображение диаграммы и суммы
    }
}