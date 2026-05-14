package com.example.cookingtimer.model.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TimerWithSteps(
    @Embedded val timer: TimerEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "timerId"
    )
    val steps: List<RecipeStepEntity>
)
