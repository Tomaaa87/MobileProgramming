package com.example.cookingtimer.model.repository

import com.example.cookingtimer.model.data.local.dao.UserDao
import com.example.cookingtimer.model.data.local.entity.UserEntity
import com.example.cookingtimer.model.data.local.util.Mapper.toDomain
import com.example.cookingtimer.presentation.view_model.profile.ProfileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : ProfileRepository {

    override fun getProfileDataFlow(): Flow<ProfileData?> {
        return userDao.getUserProfile().map { it?.toDomain() }
    }

    override suspend fun getProfileData(): Result<ProfileData> = withContext(Dispatchers.IO) {
        try {
            val user = userDao.getUserProfile().first() ?: UserEntity(
                displayName = "New Chef",
                bio = "Cooking enthusiast.",
                notificationsEnabled = true,
                emailUpdates = false,
                selectedTheme = "System",
                fontSize = 16f,
                achievements = emptyList()
            ).also { userDao.insertOrUpdateUser(it) }

            Result.success(user.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
