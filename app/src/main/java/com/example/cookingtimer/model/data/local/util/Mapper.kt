package com.example.cookingtimer.model.data.local.util

import com.example.cookingtimer.model.TimerData
import com.example.cookingtimer.model.data.local.entity.*
import com.example.cookingtimer.presentation.view_model.profile.ProfileData

object Mapper {

    fun TimerEntity.toDomain(): TimerData {
        return TimerData(
            id = this.id,
            title = this.title,
            totalSeconds = this.totalSeconds,
            remainingSeconds = this.remainingSeconds,
            description = this.description,
            isRunning = this.isRunning
        )
    }

    fun TimerData.toEntity(userId: Int, categoryId: Int? = null): TimerEntity {
        return TimerEntity(
            id = this.id,
            userId = userId,
            categoryId = categoryId,
            title = this.title,
            totalSeconds = this.totalSeconds,
            remainingSeconds = this.remainingSeconds,
            description = this.description,
            isRunning = this.isRunning
        )
    }

    fun UserEntity.toDomain(): ProfileData {
        return ProfileData(
            displayName = this.displayName,
            bio = this.bio,
            notificationsEnabled = this.notificationsEnabled,
            emailUpdates = this.emailUpdates,
            selectedTheme = this.selectedTheme,
            fontSize = this.fontSize,
            achievements = this.achievements
        )
    }

    // Category Mappings
    fun CategoryEntity.toDomain(): String = this.name

    // Ingredient Mappings
    fun IngredientEntity.toDomain(): Pair<String, String> = this.name to this.amount

    // Recipe Step Mappings
    fun RecipeStepEntity.toDomain(): String = this.instruction

}
