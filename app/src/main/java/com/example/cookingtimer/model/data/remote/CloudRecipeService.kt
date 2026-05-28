package com.example.cookingtimer.model.data.remote

import retrofit2.http.*

interface CloudRecipeService {
    @GET("recipes")
    suspend fun getRecipes(): List<CloudRecipe>

    @GET("recipes/{id}")
    suspend fun getRecipe(@Path("id") id: Int): CloudRecipe

    @POST("recipes")
    suspend fun createRecipe(@Body recipe: CloudRecipe): CloudRecipe

    @PUT("recipes/{id}")
    suspend fun updateRecipe(@Path("id") id: Int, @Body recipe: CloudRecipe): CloudRecipe

    @DELETE("recipes/{id}")
    suspend fun deleteRecipe(@Path("id") id: Int)
}
