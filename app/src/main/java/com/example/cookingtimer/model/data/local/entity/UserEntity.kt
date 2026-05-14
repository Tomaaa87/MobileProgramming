package com.example.cookingtimer.model.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey
    val id: Int = 1,
    val displayName: String,
    val bio: String,
    val notificationsEnabled: Boolean,
    val emailUpdates: Boolean,
    val selectedTheme: String,
    val fontSize: Float,
    val achievements: List<String>
)
