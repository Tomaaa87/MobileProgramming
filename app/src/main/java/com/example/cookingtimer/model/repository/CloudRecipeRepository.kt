package com.example.cookingtimer.model.repository

import com.example.cookingtimer.model.data.remote.CloudRecipe
import com.example.cookingtimer.model.data.remote.CloudRecipeService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudRecipeRepository @Inject constructor(
    private val apiService: CloudRecipeService
) {
    suspend fun getRecipes(): List<CloudRecipe> = apiService.getRecipes()

    suspend fun getRecipe(id: Int): CloudRecipe = apiService.getRecipe(id)

    suspend fun createRecipe(recipe: CloudRecipe): CloudRecipe = apiService.createRecipe(recipe)

    suspend fun updateRecipe(id: Int, recipe: CloudRecipe): CloudRecipe = apiService.updateRecipe(id, recipe)

    suspend fun deleteRecipe(id: Int) = apiService.deleteRecipe(id)
}
