package com.example.cookingtimer.model.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cookingtimer.model.data.local.dao.*
import com.example.cookingtimer.model.data.local.entity.*
import com.example.cookingtimer.model.data.local.util.Converters

@Database(
    entities = [
        TimerEntity::class,
        UserEntity::class,
        CategoryEntity::class,
        RecipeStepEntity::class,
        IngredientEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timerDao(): TimerDao
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun recipeStepDao(): RecipeStepDao
    abstract fun ingredientDao(): IngredientDao
}
