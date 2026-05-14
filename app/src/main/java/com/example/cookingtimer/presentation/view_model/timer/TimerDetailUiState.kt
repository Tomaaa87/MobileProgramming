package com.example.cookingtimer.presentation.view_model.timer

import com.example.cookingtimer.model.TimerData
import com.example.cookingtimer.model.data.local.entity.IngredientEntity
import com.example.cookingtimer.model.data.local.entity.RecipeStepEntity

sealed class TimerDetailUiState {
    object Init : TimerDetailUiState()
    object Loading : TimerDetailUiState()
    data class Success(
        val timer: TimerData,
        val steps: List<RecipeStepEntity>,
        val ingredients: List<IngredientEntity>
    ) : TimerDetailUiState()
    data class Error(val message: String) : TimerDetailUiState()
}
