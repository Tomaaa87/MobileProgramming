package com.example.cookingtimer.model.di

import com.example.cookingtimer.model.datasource.network.service.CloudRecipeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/") // Default FastAPI port. Change to 5000 if using Flask.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCloudRecipeService(retrofit: Retrofit): CloudRecipeService {
        return retrofit.create(CloudRecipeService::class.java)
    }
}
