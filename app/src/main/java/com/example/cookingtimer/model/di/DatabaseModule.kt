package com.example.cookingtimer.model.di

import android.content.Context
import androidx.room.Room
import com.example.cookingtimer.model.data.local.dao.*
import com.example.cookingtimer.model.data.local.db.AppDatabase
import com.example.cookingtimer.model.repository.*
import com.example.cookingtimer.model.repository.category.CategoryRepository
import com.example.cookingtimer.model.repository.category.CategoryRepositoryImpl
import com.example.cookingtimer.model.repository.category.FirestoreCategoryRepositoryImpl
import com.example.cookingtimer.model.repository.recipe.RecipeRepository
import com.example.cookingtimer.model.repository.recipe.RecipeRepositoryImpl
import com.example.cookingtimer.model.repository.auth.AuthRepository
import com.example.cookingtimer.model.repository.auth.AuthRepositoryImpl
import com.example.cookingtimer.model.repository.profile.ProfileRepository
import com.example.cookingtimer.model.repository.profile.ProfileRepositoryImpl
import com.example.cookingtimer.model.repository.profile.FirestoreProfileRepositoryImpl
import com.example.cookingtimer.model.repository.timer.TimerRepository
import com.example.cookingtimer.model.repository.timer.TimerRepositoryImpl
import com.example.cookingtimer.model.repository.timer.FirestoreTimerRepositoryImpl
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
    abstract fun bindProfileRepository(impl: FirestoreProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindTimerRepository(impl: FirestoreTimerRepositoryImpl): TimerRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: FirestoreCategoryRepositoryImpl): CategoryRepository

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
