package com.example.cookingtimer.di.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Boolean>
    suspend fun register(fullName: String, email: String, password: String): Result<Unit>
}
