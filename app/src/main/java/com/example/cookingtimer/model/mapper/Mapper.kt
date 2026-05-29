package com.example.cookingtimer.model.mapper

import com.example.cookingtimer.model.data.local.entity.CategoryEntity
import com.example.cookingtimer.model.data.local.entity.IngredientEntity
import com.example.cookingtimer.model.data.local.entity.RecipeStepEntity
import com.example.cookingtimer.model.data.local.entity.TimerEntity
import com.example.cookingtimer.model.data.local.entity.UserEntity
import com.example.cookingtimer.model.repository.timer.TimerData
import com.example.cookingtimer.presentation.view_model.profile.ProfileData

object Mapper {

    fun TimerEntity.toDomain(): TimerData {
        return TimerData(
            id = this.id.toString(),
            title = this.title,
            totalSeconds = this.totalSeconds,
            remainingSeconds = this.remainingSeconds,
            description = this.description,
            isRunning = this.isRunning
        )
    }

    fun TimerData.toEntity(userId: String, categoryId: Int? = null): TimerEntity {
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
            email = this.email,
            bio = this.bio,
            notificationsEnabled = this.notificationsEnabled,
            emailUpdates = this.emailUpdates,
            selectedTheme = this.selectedTheme,
            fontSize = this.fontSize,
            achievements = this.achievements
        )
    }


    fun CategoryEntity.toDomain(): String = this.name

    fun IngredientEntity.toDomain(): Pair<String, String> = this.name to this.amount

    fun RecipeStepEntity.toDomain(): String = this.instruction

}