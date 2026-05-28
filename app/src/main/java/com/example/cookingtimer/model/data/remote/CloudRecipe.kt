package com.example.cookingtimer.model.data.remote

import com.google.gson.annotations.SerializedName

data class CloudRecipe(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String
)
