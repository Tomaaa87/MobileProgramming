package com.example.cookingtimer.model.datasource.network.service

import com.example.cookingtimer.model.datasource.network.dto.CloudRecipe
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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