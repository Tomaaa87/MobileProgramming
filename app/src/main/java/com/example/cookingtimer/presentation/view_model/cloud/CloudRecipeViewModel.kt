package com.example.cookingtimer.presentation.view_model.cloud

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingtimer.model.data.remote.CloudRecipe
import com.example.cookingtimer.model.repository.CloudRecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CloudRecipeViewModel @Inject constructor(
    private val repository: CloudRecipeRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<CloudRecipe>>(emptyList())
    val recipes: StateFlow<List<CloudRecipe>> = _recipes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _recipes.value = repository.getRecipes()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addRecipe(recipe: CloudRecipe) {
        viewModelScope.launch {
            try {
                repository.createRecipe(recipe)
                loadRecipes()
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            }
        }
    }

    fun deleteRecipe(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteRecipe(id)
                loadRecipes()
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            }
        }
    }

    fun updateRecipe(id: Int, recipe: CloudRecipe) {
        viewModelScope.launch {
            try {
                repository.updateRecipe(id, recipe)
                loadRecipes()
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            }
        }
    }
}
