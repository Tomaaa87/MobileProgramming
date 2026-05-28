package com.example.cookingtimer.model.data.local.dao

import androidx.room.*
import com.example.cookingtimer.model.data.local.entity.TimerEntity
import com.example.cookingtimer.model.data.local.entity.TimerWithSteps
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {
    @Query("SELECT * FROM timers WHERE userId = :userId")
    fun getAllTimers(userId: String): Flow<List<TimerEntity>>

    @Transaction
    @Query("SELECT * FROM timers WHERE userId = :userId")
    fun getTimersWithSteps(userId: String): Flow<List<TimerWithSteps>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimer(timer: TimerEntity): Long

    @Update
    suspend fun updateTimer(timer: TimerEntity)

    @Delete
    suspend fun deleteTimer(timerEntity: TimerEntity)

    @Query("SELECT * FROM timers WHERE id = :id")
    suspend fun getTimerById(id: String): TimerEntity?

    @Transaction
    @Query("SELECT * FROM timers WHERE id = :id")
    suspend fun getTimerWithStepsById(id: String): TimerWithSteps?
}
