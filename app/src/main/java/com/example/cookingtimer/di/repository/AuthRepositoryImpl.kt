package com.example.cookingtimer.di.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Boolean> = withContext(Dispatchers.IO) {

        delay(1000)
        if (email == "test@gmail.com" && password == "123456") {
            Result.success(true)
        } else {
            Result.failure(Exception("Invalid email or password"))
        }
    }

    override suspend fun register(fullName: String, email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        delay(1000)
        if (fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Please fill in all fields."))
        }
    }
}
