package com.example.cookingtimer.model.repository.category

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.example.cookingtimer.model.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreCategoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CategoryRepository {

    private val categoriesCollection = firestore.collection("categories")

    override fun getCategoriesFlow(): Flow<List<CategoryEntity>> {
        return categoriesCollection.snapshots().map { snapshot ->
            if (snapshot.isEmpty) {
                // Return a default list if Firestore is empty
                listOf(CategoryEntity(id = 1, name = "Italian"), CategoryEntity(id = 2, name = "Breakfast"))
            } else {
                snapshot.documents.map { doc ->
                    CategoryEntity(
                        id = doc.id.hashCode(),
                        name = doc.getString("name") ?: ""
                    )
                }
            }
        }
    }

    override suspend fun getCategories(): Result<List<CategoryEntity>> = try {
        val snapshot = categoriesCollection.get().await()
        if (snapshot.isEmpty) {
             Result.success(listOf(CategoryEntity(id = 1, name = "Italian"), CategoryEntity(id = 2, name = "Breakfast")))
        } else {
            val categories = snapshot.documents.map { doc ->
                CategoryEntity(
                    id = doc.id.hashCode(),
                    name = doc.getString("name") ?: ""
                )
            }
            Result.success(categories)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addCategory(name: String): Result<Unit> = try {
        val data = hashMapOf("name" to name)
        categoriesCollection.add(data).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteCategory(id: Int): Result<Unit> {
        return Result.failure(Exception("Direct delete by hashcode not supported in Firestore demo"))
    }
}
