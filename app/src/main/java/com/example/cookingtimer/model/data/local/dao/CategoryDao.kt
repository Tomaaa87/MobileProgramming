package com.example.cookingtimer.model.data.local.dao

import androidx.room.*
import com.example.cookingtimer.model.data.local.entity.CategoryEntity
import com.example.cookingtimer.model.data.local.entity.CategoryWithTimers
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Transaction
    @Query("SELECT * FROM categories")
    fun getCategoriesWithTimers(): Flow<List<CategoryWithTimers>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryEntity?
}
