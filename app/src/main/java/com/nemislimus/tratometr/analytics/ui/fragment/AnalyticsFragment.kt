package com.nemislimus.tratometr.analytics.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.nemislimus.tratometr.R
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentAnalyticsBinding
import java.math.BigDecimal

class AnalyticsFragment : BindingFragment<FragmentAnalyticsBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAnalyticsBinding {
        return FragmentAnalyticsBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val sumList = mutableListOf<BigDecimal>(BigDecimal("500"), BigDecimal("300"), BigDecimal("100"), BigDecimal("50")) // Значения для диаграммы
        val colorsList = mutableListOf<Int>(
            ContextCompat.getColor(context, R.color.chart_color_1),
            ContextCompat.getColor(context, R.color.chart_color_2),
            ContextCompat.getColor(context, R.color.chart_color_3),
            ContextCompat.getColor(context, R.color.chart_color_4),
        )

        binding.ringChart.setData(colorsList, sumList)

    }
}