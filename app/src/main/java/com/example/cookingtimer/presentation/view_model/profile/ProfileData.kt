package com.example.cookingtimer.presentation.view_model.profile

data class ProfileData(
    val displayName: String,
    val bio: String,
    val notificationsEnabled: Boolean,
    val emailUpdates: Boolean,
    val selectedTheme: String,
    val fontSize: Float,
    val achievements: List<String>
)
