package com.example.cookingtimer.presentation.util

object AuthValidators {
    fun validateEmail(email: String): String? {
        return if (email.isBlank()) "Email is required"
        else if (!email.contains("@")) "Invalid email format"
        else null
    }

    fun validatePassword(password: String): String? {
        return if (password.isBlank()) "Password is required"
        else if (password.length < 6) "Password must be at least 6 characters"
        else null
    }

    fun validateName(name: String): String? {
        return if (name.isBlank()) "Name is required" else null
    }
}
