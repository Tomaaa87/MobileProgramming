package com.example.cookingtimer.model.repository

import com.example.cookingtimer.model.data.local.dao.UserDao
import com.example.cookingtimer.model.data.local.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Boolean> = withContext(Dispatchers.IO) {
        delay(1000)
        // Allow login if email and password are provided
        if (email.isNotBlank() && password.length >= 6) {
            Result.success(true)
        } else {
            Result.failure(Exception("Invalid email or password"))
        }
    }

    override suspend fun register(fullName: String, email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        delay(1000)
        if (fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
            try {
                userDao.insertOrUpdateUser(
                    UserEntity(
                        id = 1, // Fixed ID for local user profile
                        displayName = fullName,
                        bio = "New cooking enthusiast!",
                        notificationsEnabled = true,
                        emailUpdates = false,
                        selectedTheme = "System",
                        fontSize = 16f,
                        achievements = emptyList()
                    )
                )
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(Exception("Please fill in all fields."))
        }
    }
}
