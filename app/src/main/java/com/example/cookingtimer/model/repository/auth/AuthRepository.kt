package com.example.cookingtimer.model.repository.auth

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Boolean>
    suspend fun register(fullName: String, email: String, password: String): Result<Unit>
    suspend fun logout()
}