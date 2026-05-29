package com.example.cookingtimer.model.data.local.dao

import androidx.room.*
import com.example.cookingtimer.model.data.local.entity.RecipeStepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeStepDao {
    @Query("SELECT * FROM recipe_steps WHERE timerId = :timerId ORDER BY stepNumber ASC")
    fun getStepsForTimer(timerId: String): Flow<List<RecipeStepEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(step: RecipeStepEntity)

    @Update
    suspend fun updateStep(step: RecipeStepEntity)

    @Delete
    suspend fun deleteStep(step: RecipeStepEntity)

    @Query("DELETE FROM recipe_steps WHERE timerId = :timerId")
    suspend fun deleteAllStepsForTimer(timerId: String)
}
