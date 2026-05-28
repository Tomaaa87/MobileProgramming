package com.example.cookingtimer.model.data.local.dao

import androidx.room.*
import com.example.cookingtimer.model.data.local.entity.IngredientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients WHERE timerId = :timerId")
    fun getIngredientsForTimer(timerId: String): Flow<List<IngredientEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: IngredientEntity)

    @Update
    suspend fun updateIngredient(ingredient: IngredientEntity)

    @Delete
    suspend fun deleteIngredient(ingredient: IngredientEntity)

    @Query("DELETE FROM ingredients WHERE timerId = :timerId")
    suspend fun deleteAllIngredientsForTimer(timerId: String)
}
