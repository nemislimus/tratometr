package com.nemislimus.tratometr.common.util

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object FieldValidator {

    const val INVALID_EMAIL = "Некорректный e-mail"
    const val REQUIRED_FIELD = "Обязательное поле!"
    const val NOT_MATCH = "Пароли не совпадают"
    const val SHORT_PASSWORD = "Пароль должен содержать не менее 6 символов"
    const val MIN_PASSWORD_LENGTH = 6

    fun isValidEmail(email: String): Boolean {
        val emailRegexPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return Regex(emailRegexPattern).matches(email)
    }

    fun validateEmail(
        emailField: TextInputLayout,
        emailText: TextInputEditText
    ): Boolean {
        return if (emailText.text.toString().trim().isEmpty()) {
            emailField.error = REQUIRED_FIELD
            false
        } else if (!isValidEmail(emailText.text.toString())) {
            emailField.error = INVALID_EMAIL
            false
        } else {
            emailField.isErrorEnabled = false
            true
        }
    }

    fun validatePassword(
        passwordField: TextInputLayout,
        passwordText: TextInputEditText
    ): Boolean {
        return if (passwordText.text.toString().trim().isEmpty()) {
            passwordField.error = REQUIRED_FIELD
            false
        } else if (passwordText.text.toString().length < MIN_PASSWORD_LENGTH) {
            passwordField.error = SHORT_PASSWORD
            false
        } else {
            passwordField.isErrorEnabled = false
            true
        }
    }

    fun validatePasswordMatch(
        passwordText: TextInputEditText,
        repeatPasswordField: TextInputLayout,
        repeatPasswordText: TextInputEditText
    ): Boolean {
        val password = passwordText.text.toString()
        val repeatPassword = repeatPasswordText.text.toString()

        return when {
            password.isEmpty() || repeatPassword.isEmpty() -> {
                repeatPasswordField.error = null
                false
            }

            password != repeatPassword -> {
                repeatPasswordField.error = NOT_MATCH
                false
            }

            else -> {
                repeatPasswordField.error = null
                true
            }
        }
    }
}