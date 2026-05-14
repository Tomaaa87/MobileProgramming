package com.example.cookingtimer.model.repository

import com.example.cookingtimer.model.data.local.entity.IngredientEntity
import com.example.cookingtimer.model.data.local.entity.RecipeStepEntity

interface RecipeRepository {
    suspend fun getIngredients(timerId: Int): Result<List<IngredientEntity>>
    suspend fun getSteps(timerId: Int): Result<List<RecipeStepEntity>>
    suspend fun addIngredient(timerId: Int, name: String, amount: String): Result<Unit>
    suspend fun addStep(timerId: Int, instruction: String, stepNumber: Int): Result<Unit>
}
