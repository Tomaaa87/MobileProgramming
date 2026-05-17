package com.example.cookingtimer.model.repository.auth

import com.example.cookingtimer.model.data.local.dao.UserDao
import com.example.cookingtimer.model.data.local.entity.UserEntity
import com.example.cookingtimer.model.repository.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            delay(1000)
            val user = userDao.getUserByEmail(email)
            if (user != null && user.password == password) {
                sessionManager.login(user.id)
                Result.success(true)
            } else if (user != null && user.password != password) {
                Result.failure(Exception("Incorrect password"))
            } else {
                Result.failure(Exception("User not found. Please register first."))
            }
        }

    override suspend fun register(fullName: String, email: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            delay(1000)
            if (fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                try {
                    // Check if user already exists
                    if (userDao.getUserByEmail(email) != null) {
                        return@withContext Result.failure(Exception("Email already registered"))
                    }

                    val userId = userDao.insertOrUpdateUser(
                        UserEntity(
                            email = email,
                            password = password,
                            displayName = fullName,
                            bio = "New cooking enthusiast!",
                            notificationsEnabled = true,
                            emailUpdates = false,
                            selectedTheme = "System",
                            fontSize = 16f,
                            achievements = emptyList()
                        )
                    )
                    sessionManager.login(userId.toInt())
                    Result.success(Unit)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            } else {
                Result.failure(Exception("Please fill in all fields."))
            }
        }

    override suspend fun logout() {
        sessionManager.logout()
    }
}