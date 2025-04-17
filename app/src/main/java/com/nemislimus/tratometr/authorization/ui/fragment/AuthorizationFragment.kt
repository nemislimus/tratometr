package com.nemislimus.tratometr.authorization.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nemislimus.tratometr.R
import com.nemislimus.tratometr.authorization.domain.models.Resource
import com.nemislimus.tratometr.authorization.ui.viewmodel.AuthorizationViewModel
import com.nemislimus.tratometr.common.appComponent
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentAuthorizationBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthorizationFragment : BindingFragment<FragmentAuthorizationBinding>() {

    @Inject
    lateinit var vmFactory: AuthorizationViewModel.Factory
    private lateinit var viewModel: AuthorizationViewModel

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAuthorizationBinding {
        return FragmentAuthorizationBinding.inflate(inflater, container, false)
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity(), vmFactory)[AuthorizationViewModel::class]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emailText.addTextChangedListener(TextFieldValidation(binding.emailText))
        binding.passwordText.addTextChangedListener(TextFieldValidation(binding.passwordText))

        binding.createAccount.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_registrationFragment)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailText.text.toString()
            val password = binding.passwordText.text.toString()

            lifecycleScope.launch {
                val response = viewModel.authorization(email, password)
                when (response) {
                    is Resource.Success -> {
                        viewModel.putTokensToStorage(response.value!!)
                        findNavController().navigate(R.id.action_authorizationFragment_to_expensesFragment)
                    }

                    is Resource.Error -> {
                        binding.emailText.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.error
                            )
                        )
                        binding.emailField.error = INVALID_EMAIL
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
                    validateEmail(binding.emailField, binding.emailText)
                    binding.emailText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
                }

                R.id.passwordText -> {
                    validatePassword(binding.passwordField, binding.passwordText)
                }
            }
            updateLoginButtonState()
        }
    }

    private fun updateLoginButtonState() {
        val isEmailValid = validateEmail(binding.emailField, binding.emailText)
        val isPasswordValid = validatePassword(
            binding.passwordField,
            binding.passwordText
        )
        binding.loginButton.isEnabled = isEmailValid && isPasswordValid
    }

    companion object {
        const val INVALID_EMAIL = "Некорректный e-mail"
        const val REQUIRED_FIELD = "Обязательное поле!"
        const val NOT_MATCH = "Пароли не совпадают"
        const val SHORT_PASSWORD = "Пароль должен содержать не менее 6 символов"
        const val MIN_PASSWORD_LENGTH = 6

        fun isValidEmail(email: String): Boolean {
            val emailRegexPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            return Regex(emailRegexPattern).matches(email)
        }

        fun validateEmail(textInputLayout: TextInputLayout, editText: TextInputEditText): Boolean {
            if (editText.text.toString().trim().isEmpty()) {
                textInputLayout.error = REQUIRED_FIELD
                return false
            } else if (!isValidEmail(editText.text.toString())) {
                textInputLayout.error = INVALID_EMAIL
                return false
            } else {
                textInputLayout.isErrorEnabled = false
            }
            return true
        }

        fun validatePassword(
            textInputLayout: TextInputLayout,
            editText: TextInputEditText
        ): Boolean {
            if (editText.text.toString().trim().isEmpty()) {
                textInputLayout.error = REQUIRED_FIELD
                return false
            } else if (editText.text.toString().length < MIN_PASSWORD_LENGTH) {
                textInputLayout.error = SHORT_PASSWORD
                return false
            } else {
                textInputLayout.isErrorEnabled = false
            }
            return true
        }
    }
}