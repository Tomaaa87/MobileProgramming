package com.example.cookingtimer.model.repository.profile

import com.example.cookingtimer.presentation.view_model.profile.ProfileData
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfileDataFlow(): Flow<ProfileData?>
    suspend fun getProfileData(): Result<ProfileData>
    suspend fun updateProfileData(data: ProfileData): Result<Unit>
}
