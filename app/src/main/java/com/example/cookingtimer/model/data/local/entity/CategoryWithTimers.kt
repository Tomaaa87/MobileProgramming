package com.example.cookingtimer.model.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithTimers(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val timers: List<TimerEntity>
)
