package com.example.cookingtimer.model.repository.profile

import com.example.cookingtimer.model.data.local.dao.UserDao
import com.example.cookingtimer.model.data.local.entity.UserEntity
import com.example.cookingtimer.model.mapper.Mapper.toDomain
import com.example.cookingtimer.model.repository.SessionManager
import com.example.cookingtimer.presentation.view_model.profile.ProfileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : ProfileRepository {

    override fun getProfileDataFlow(): Flow<ProfileData?> {
        val userId = sessionManager.getUserId()
        return userDao.getUserProfile(userId).map { it?.toDomain() }
    }

    override suspend fun getProfileData(): Result<ProfileData> = withContext(Dispatchers.IO) {
        try {
            val userId = sessionManager.getUserId()
            val user = userDao.getUserProfile(userId).first() ?: UserEntity(
                id = userId,
                email = "default@example.com",
                password = "password",
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

    override suspend fun updateProfileData(data: ProfileData): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Since we are primarily using Firestore for Profile now,
            // this local Room implementation can be a stub or simple success.
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
