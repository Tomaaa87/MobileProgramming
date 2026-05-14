package com.example.cookingtimer.model.repository

import com.example.cookingtimer.model.data.local.entity.CategoryEntity

interface CategoryRepository {
    suspend fun getCategories(): Result<List<CategoryEntity>>
    suspend fun addCategory(name: String): Result<Unit>
    suspend fun deleteCategory(id: Int): Result<Unit>
}
