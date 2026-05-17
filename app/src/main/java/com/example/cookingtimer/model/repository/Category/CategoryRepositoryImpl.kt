package com.example.cookingtimer.model.repository.category

import com.example.cookingtimer.model.data.local.dao.CategoryDao
import com.example.cookingtimer.model.data.local.entity.CategoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getCategoriesFlow(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategories()
    }

    override suspend fun getCategories(): Result<List<CategoryEntity>> =
        withContext(Dispatchers.IO) {
            try {
                var categories = categoryDao.getAllCategories().first()
                if (categories.isEmpty()) {
                    val seed = listOf(
                        "Breakfast", "Lunch", "Dinner", "Dessert",
                        "Snacking", "Healthy", "Vegan", "Italian", "Baking"
                    )
                    seed.forEach { categoryDao.insertCategory(CategoryEntity(name = it)) }
                    categories = categoryDao.getAllCategories().first()
                }
                Result.success(categories)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun addCategory(name: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            categoryDao.insertCategory(CategoryEntity(name = name))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCategory(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val category = categoryDao.getCategoryById(id)
            if (category != null) {
                categoryDao.deleteCategory(category)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Category not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}