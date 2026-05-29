package com.example.cookingtimer.model.datasource.network.mapper
import com.example.cookingtimer.model.datasource.network.dto.CloudRecipe

fun CloudRecipe.toDomain(): CloudRecipe {
    return CloudRecipe(
        id = this.id,
        title = this.title,
        description = this.description
    )
}

fun CloudRecipe.toDto(): CloudRecipe {
    return CloudRecipe(
        id = this.id,
        title = this.title,
        description = this.description
    )
}