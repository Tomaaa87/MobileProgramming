package com.example.cookingtimer.model.repository

import com.example.cookingtimer.presentation.view_model.profile.ProfileData
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfileDataFlow(): Flow<ProfileData?>
    suspend fun getProfileData(): Result<ProfileData>
}
