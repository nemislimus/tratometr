package com.nemislimus.tratometr.settings.ui.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.nemislimus.tratometr.settings.ui.receiver.ReminderReceiver
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
        setNotification(hours, minutes)
    }

    private suspend fun reminderStop() {
        withContext(Dispatchers.IO) { updateSettingsParams("") }
        cancelNotification()
    }

    private fun setNotification(hour: Int, minute: Int) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = createReminderPendingIntent(requireContext())

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun cancelNotification() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = createReminderPendingIntent(requireContext())
        alarmManager.cancel(pendingIntent)
    }

    private fun createReminderPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = "DAILY_REMINDER"
        }
        return PendingIntent.getBroadcast(
            context,
            REMINDER_INTENT_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }


    companion object {
        const val TIME_PICKER_TAG = "timepicker_tag"
        const val REMINDER_INTENT_CODE = 13
    }

}