package com.example.cookingtimer.di.repository

import com.example.cookingtimer.presentation.view_model.profile.ProfileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor() : ProfileRepository {
    override suspend fun getProfileData(): Result<ProfileData> = withContext(Dispatchers.IO) {
        delay(1000)
        Result.success(
            ProfileData(
                displayName = "Tom Smith",
                bio = "Home cook and pasta lover.",
                notificationsEnabled = true,
                emailUpdates = false,
                selectedTheme = "System",
                fontSize = 16f,
                achievements = listOf("Timer Pro", "Egg Master", "Pasta King", "Chef de Cuisine", "Quick Learner")
            )
        )
    }
}
