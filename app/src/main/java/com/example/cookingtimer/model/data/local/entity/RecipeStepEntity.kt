package com.example.cookingtimer.model.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipe_steps",
    foreignKeys = [
        ForeignKey(
            entity = TimerEntity::class,
            parentColumns = ["id"],
            childColumns = ["timerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["timerId"])]
)
data class RecipeStepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timerId: String,
    val stepNumber: Int,
    val instruction: String
)
