package com.nemislimus.tratometr.settings.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nemislimus.tratometr.common.App
import com.nemislimus.tratometr.common.appComponent
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentSettingsBinding
import com.nemislimus.tratometr.settings.domain.model.SettingsParams
import com.nemislimus.tratometr.settings.ui.viewmodel.SettingsFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    @Inject
    lateinit var vmFactory: SettingsFragmentViewModel.Factory
    private val viewModel: SettingsFragmentViewModel by viewModels { vmFactory }
    private var themeChangingJob: Job? = null

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeSettingsParams().observe(viewLifecycleOwner) { settingsPrams ->
            render(settingsPrams)
        }

        // Обработка смены положения "Напоминание"
        binding.swReminder.setOnCheckedChangeListener { _, checked ->
            binding.tvTimeRemind.isVisible = checked
        }

        // Обработка смены положения "Тема"
        binding.swTheme.setOnCheckedChangeListener { _, checked ->
            clickOnThemeSwitch(checked)
        }

        binding.btnExport.setOnClickListener { }
        binding.btnLogOut.setOnClickListener { }

        // Нажатие на кнопку назад у тулбара
        binding.tbSettings.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun render(params: SettingsParams) {
        themeSwitchRender(params.isDarkMode)
    }

    private fun clickOnThemeSwitch(checked: Boolean) {
        themeChangingJob?.cancel()

        themeChangingJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val params: SettingsParams
            withContext(Dispatchers.Main) {
                params = pickSettingsParams()
            }
            viewModel.updateSettings(params)
            viewModel.getSettingsParams()
            delay(600)
            withContext(Dispatchers.Main) {
                switchAppTheme(checked)
            }
        }
    }

    private fun switchAppTheme(isDarkMode: Boolean) {
        (requireActivity().applicationContext as App).switchTheme(isDarkMode)
    }

    private fun themeSwitchRender(isDarkTheme: Boolean) {
        with(binding) {
            binding.swTheme.isChecked = isDarkTheme
            ivDarkThemeIcon.isVisible = isDarkTheme
            ivLightThemeIcon.isVisible = !isDarkTheme
        }
    }

    private fun pickSettingsParams(): SettingsParams {
        return SettingsParams(
            remindTime = binding.tvTimeRemind.text.toString(),
            isDarkMode = binding.swTheme.isChecked,
        )
    }

}