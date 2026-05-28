package com.example.cookingtimer.model.repository.recipe

import com.example.cookingtimer.model.data.local.entity.IngredientEntity
import com.example.cookingtimer.model.data.local.entity.RecipeStepEntity

interface RecipeRepository {
    suspend fun getIngredients(timerId: String): Result<List<IngredientEntity>>
    suspend fun getSteps(timerId: String): Result<List<RecipeStepEntity>>
    suspend fun addIngredient(timerId: String, name: String, amount: String): Result<Unit>
    suspend fun addStep(timerId: String, instruction: String, stepNumber: Int): Result<Unit>
}
