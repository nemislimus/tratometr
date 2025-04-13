package com.nemislimus.tratometr.settings.ui.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.nemislimus.tratometr.R
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
import java.util.Calendar
import javax.inject.Inject

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    @Inject
    lateinit var vmFactory: SettingsFragmentViewModel.Factory
    private val viewModel: SettingsFragmentViewModel by viewModels { vmFactory }
    private var themeChangingJob: Job? = null
    private var reminderChangingJob: Job? = null
    private var timePicker: MaterialTimePicker? = null
    private val systemVersionWithPermissions = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionIsGranted: Boolean ->
            if (permissionIsGranted) {
                binding.swReminder.isEnabled = true
            } else {
                binding.swReminder.isEnabled = false

                if (systemVersionWithPermissions) {
                    val shouldShowRequestPermission =
                        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)

                    if (!shouldShowRequestPermission) {
                        Toast.makeText(requireContext(), PERMISSION_MESSAGE, Toast.LENGTH_SHORT ).show()
                        getPermissionAtSettings()
                    }
                }
            }
        }


    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeSettingsParams().observe(viewLifecycleOwner) { settingsPrams ->
            stateProcessing(settingsPrams)
        }

        setUiListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            delay(500)
            checkNotificationPermission()
        }
    }

    override fun onDestroyFragment() {
        timePicker?.dismiss()
        timePicker = null

        themeChangingJob?.cancel()
        themeChangingJob = null

        reminderChangingJob?.cancel()
        reminderChangingJob = null
    }

    private fun setUiListeners() {
        binding.swReminder.setOnClickListener {
            clickOnReminderSwitch(binding.swReminder.isChecked)
        }

        binding.swTheme.setOnClickListener {
            clickOnThemeSwitch(binding.swTheme.isChecked)
        }

        binding.btnExport.setOnClickListener { }
        binding.btnLogOut.setOnClickListener { }

        binding.tbSettings.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun stateProcessing(params: SettingsParams) {
        themeSwitchRender(params.isDarkMode)
        reminderSwitchRender(params.remindTime)
    }

    private suspend fun updateSettingsParams(
        reminderTime: String = binding.tvTimeRemind.text.toString(),
        isDarkMode: Boolean = binding.swTheme.isChecked,
    ) {
        val params: SettingsParams = withContext(Dispatchers.Main) {
            pickSettingsParams(reminderTime, isDarkMode)
        }
        viewModel.saveSettings(params)
        viewModel.getSettingsParams()
    }

    private fun clickOnReminderSwitch(checked: Boolean) {
        reminderChangingJob?.cancel()
        reminderChangingJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            delay(600)
            withContext(Dispatchers.Main) {
                if (binding.tvTimeRemind.text.isBlank()) showTimePickerDialog(checked)
                if (!checked) reminderStop()
            }
        }
    }

    private fun clickOnThemeSwitch(checked: Boolean) {
        themeChangingJob?.cancel()
        themeChangingJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            updateSettingsParams()
            delay(600)
            withContext(Dispatchers.Main) {
                switchAppTheme(checked)
            }
        }
    }

    private fun themeSwitchRender(isDarkTheme: Boolean) {
        with(binding) {
            binding.swTheme.isChecked = isDarkTheme
            ivDarkThemeIcon.isVisible = isDarkTheme
            ivLightThemeIcon.isVisible = !isDarkTheme
        }
    }

    private fun reminderSwitchRender(timeStringValue: String) {
        binding.tvTimeRemind.text = timeStringValue
        binding.swReminder.isChecked = timeStringValue.isNotBlank()
    }

    private fun pickSettingsParams(reminderTime: String, isDarkMode: Boolean): SettingsParams {
        return SettingsParams(
            remindTime = reminderTime,
            isDarkMode = isDarkMode,
        )
    }

    private fun switchAppTheme(isDarkMode: Boolean) {
        (requireActivity().applicationContext as App).switchTheme(isDarkMode)
    }

    private fun showTimePickerDialog(checked: Boolean) {
        if (checked) {
            val title = getString(R.string.select_time)
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hour)
                .setMinute(minute).setTitleText(title)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK).build()

            setTimePickerListeners(timePicker)
            timePicker?.show(parentFragmentManager, TIME_PICKER_TAG)

        }
    }

    private fun setTimePickerListeners(picker: MaterialTimePicker?) {
        picker?.addOnPositiveButtonClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                reminderStart(picker.hour, picker.minute)
            }
        }

        picker?.addOnNegativeButtonClickListener {
            binding.swReminder.isChecked = false
        }

        picker?.addOnCancelListener {
            binding.swReminder.isChecked = false
        }
    }

    private suspend fun reminderStart(hours: Int, minutes: Int) {
        val timeStringValue = viewModel.correctTimeString(hours, minutes)
        withContext(Dispatchers.IO) { updateSettingsParams(timeStringValue) }
        viewModel.setNotification(requireContext(), hours, minutes)
    }

    private suspend fun reminderStop() {
        withContext(Dispatchers.IO) { updateSettingsParams("") }
        viewModel.cancelNotification(requireContext())
    }

    private fun checkNotificationPermission() {
        if (systemVersionWithPermissions) {
            val permissionProvided = ContextCompat
                .checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)

            if (permissionProvided == PackageManager.PERMISSION_GRANTED) {
                binding.swReminder.isEnabled = true
            } else if (permissionProvided == PackageManager.PERMISSION_DENIED) {
                binding.swReminder.isEnabled = false
                if (!binding.swReminder.isEnabled) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun getPermissionAtSettings(context: Context = requireContext()) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data= Uri.fromParts("package", context.packageName, null)
        context.startActivity(intent)
    }

    companion object {
        const val TIME_PICKER_TAG = "timepicker_tag"
        const val PERMISSION_MESSAGE = "Разрешите делать напоминания!"
    }

}