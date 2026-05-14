package com.example.cookingtimer.model.data.local.dao

import androidx.room.*
import com.example.cookingtimer.model.data.local.entity.TimerEntity
import com.example.cookingtimer.model.data.local.entity.TimerWithSteps
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {
    @Query("SELECT * FROM timers")
    fun getAllTimers(): Flow<List<TimerEntity>>

    @Transaction
    @Query("SELECT * FROM timers")
    fun getTimersWithSteps(): Flow<List<TimerWithSteps>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimer(timer: TimerEntity): Long

    @Update
    suspend fun updateTimer(timer: TimerEntity)

    @Delete
    suspend fun deleteTimer(timer: TimerEntity)

    @Query("SELECT * FROM timers WHERE id = :id")
    suspend fun getTimerById(id: Int): TimerEntity?

    @Transaction
    @Query("SELECT * FROM timers WHERE id = :id")
    suspend fun getTimerWithStepsById(id: Int): TimerWithSteps?
}
