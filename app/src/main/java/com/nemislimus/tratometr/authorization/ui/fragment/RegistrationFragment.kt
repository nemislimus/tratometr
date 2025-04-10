package com.nemislimus.tratometr.authorization.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nemislimus.tratometr.R
import com.nemislimus.tratometr.authorization.domain.models.Resource
import com.nemislimus.tratometr.authorization.ui.viewmodel.RegistrationViewModel
import com.nemislimus.tratometr.common.appComponent
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentRegistrationBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationFragment : BindingFragment<FragmentRegistrationBinding>() {

    @Inject
    lateinit var vmFactory: RegistrationViewModel.Factory
    lateinit var viewModel: RegistrationViewModel

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegistrationBinding {
        return FragmentRegistrationBinding.inflate(inflater, container, false)
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity(), vmFactory)[RegistrationViewModel::class]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emailText.addTextChangedListener(TextFieldValidation(binding.emailText))
        binding.passwordText.addTextChangedListener(TextFieldValidation(binding.passwordText))
        binding.repeatPasswordText.addTextChangedListener(TextFieldValidation(binding.repeatPasswordText))

        binding.registrationButton.setOnClickListener {
            val email = binding.emailText.text.toString()
            val password = binding.passwordText.text.toString()

            lifecycleScope.launch {
                val response = viewModel.registration(email,password)

                when (response) {
                    is Resource.Success -> {
                        viewModel.putTokensToStorage(response.value!!)
                        findNavController().navigate(R.id.action_registrationFragment_to_expensesFragment)
                    }

                    is Resource.Error -> {
                        binding.errorText.text = response.message
                        binding.errorText.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    inner class TextFieldValidation(
        private val view: View,
    ) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {

            when (view.id) {
                R.id.emailText -> {
                    validateEmail()
                }

                R.id.passwordText -> {
                    validatePassword()
                    passwordsCheck()
                }

                R.id.repeatPasswordText -> {
                    passwordsCheck()
                }
            }
            updateButtonState()

        }
    }

    private fun validatePassword(): Boolean {
        if (binding.passwordText.text.toString().trim().isEmpty()) {
            binding.passwordField.error = "Required Field!"
            return false
        } else if (binding.passwordText.text.toString().length < 6) {
            binding.passwordField.error = "password can't be less than 6"
            return false
        } else {
            binding.passwordField.isErrorEnabled = false
        }
        return true
    }

    private fun validateEmail(): Boolean {
        if (binding.emailText.text.toString().trim().isEmpty()) {
            binding.emailField.error = "Required Field!"
            return false
        } else if (!isValidEmail(binding.emailText.text.toString())) {
            binding.emailField.error = "Invalid Email!"
            return false
        } else {
            binding.emailField.isErrorEnabled = false
        }
        return true
    }

    private fun passwordsCheck(): Boolean {
        val password = binding.passwordText.text.toString()
        val repeatPassword = binding.repeatPasswordText.text.toString()

        return when {
            password.isEmpty() || repeatPassword.isEmpty() -> {
                binding.repeatPasswordField.error = null
                false
            }

            password != repeatPassword -> {
                binding.repeatPasswordField.error = "Пароли не совпадают"
                false
            }

            else -> {
                binding.repeatPasswordField.error = null
                true
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegexPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return Regex(emailRegexPattern).matches(email)
    }

    private fun updateButtonState() {
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()
        val arePasswordsMatch = passwordsCheck()

        binding.registrationButton.isEnabled = isEmailValid && isPasswordValid && arePasswordsMatch
    }
}