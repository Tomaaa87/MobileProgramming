package com.example.cookingtimer.di.repository

import com.example.cookingtimer.presentation.view_model.profile.ProfileData

interface ProfileRepository {
    suspend fun getProfileData(): Result<ProfileData>
}
