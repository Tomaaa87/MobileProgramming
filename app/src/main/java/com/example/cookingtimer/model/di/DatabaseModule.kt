package com.example.cookingtimer.model.di

import android.content.Context
import androidx.room.Room
import com.example.cookingtimer.model.data.local.dao.*
import com.example.cookingtimer.model.data.local.db.AppDatabase
import com.example.cookingtimer.model.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindTimerRepository(impl: TimerRepositoryImpl): TimerRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindRecipeRepository(impl: RecipeRepositoryImpl): RecipeRepository

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "cooking_timer_db"
            )
            .fallbackToDestructiveMigration(true)
            .build()
        }

        @Provides
        @Singleton
        fun provideTimerDao(database: AppDatabase): TimerDao {
            return database.timerDao()
        }

        @Provides
        @Singleton
        fun provideUserDao(database: AppDatabase): UserDao {
            return database.userDao()
        }

        @Provides
        @Singleton
        fun provideCategoryDao(database: AppDatabase): CategoryDao {
            return database.categoryDao()
        }

        @Provides
        @Singleton
        fun provideRecipeStepDao(database: AppDatabase): RecipeStepDao {
            return database.recipeStepDao()
        }

        @Provides
        @Singleton
        fun provideIngredientDao(database: AppDatabase): IngredientDao {
            return database.ingredientDao()
        }
    }
}
