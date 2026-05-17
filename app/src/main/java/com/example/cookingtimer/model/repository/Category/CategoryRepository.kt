package com.example.cookingtimer.model.repository.category

import com.example.cookingtimer.model.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategoriesFlow(): Flow<List<CategoryEntity>>
    suspend fun getCategories(): Result<List<CategoryEntity>>
    suspend fun addCategory(name: String): Result<Unit>
    suspend fun deleteCategory(id: Int): Result<Unit>
}