package com.example.cookingtimer.model.repository.timer

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.example.cookingtimer.model.data.local.entity.TimerWithSteps
import com.example.cookingtimer.model.repository.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class FirestoreTimerRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager
) : TimerRepository {

    private val timersCollection = firestore.collection("timers")

    override fun getTimersFlow(): Flow<List<TimerData>> {
        return sessionManager.currentUserId.flatMapLatest { userId ->
            if (userId.isNullOrEmpty()) {
                flowOf(emptyList())
            } else {
                timersCollection
                    .whereEqualTo("userId", userId)
                    .snapshots()
                    .map { snapshot ->
                        snapshot.documents.map { doc ->
                            TimerData(
                                id = doc.id,
                                title = doc.getString("title") ?: "",
                                totalSeconds = doc.getLong("totalSeconds") ?: 0L,
                                remainingSeconds = doc.getLong("remainingSeconds") ?: 0L,
                                description = doc.getString("description") ?: "",
                                isRunning = doc.getBoolean("isRunning") ?: false
                            )
                        }
                    }
            }
        }
    }

    override suspend fun getTimers(): Result<List<TimerData>> = try {
        val userId = sessionManager.getUserId()
        if (userId.isEmpty()) {
            Result.success(emptyList())
        } else {
            val snapshot = timersCollection.whereEqualTo("userId", userId).get().await()
            val timers = snapshot.documents.map { doc ->
                TimerData(
                    id = doc.id,
                    title = doc.getString("title") ?: "",
                    totalSeconds = doc.getLong("totalSeconds") ?: 0L,
                    remainingSeconds = doc.getLong("remainingSeconds") ?: 0L,
                    description = doc.getString("description") ?: "",
                    isRunning = doc.getBoolean("isRunning") ?: false
                )
            }
            Result.success(timers)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addTimer(
        name: String,
        totalSeconds: Long,
        category: String,
        isFavorite: Boolean,
        ingredients: List<Pair<String, String>>,
        steps: List<String>
    ): Result<String> = try {
        val userId = sessionManager.getUserId()
        val data = hashMapOf(
            "userId" to userId,
            "title" to name,
            "totalSeconds" to totalSeconds,
            "remainingSeconds" to totalSeconds,
            "description" to "Category: $category",
            "isRunning" to false
        )
        val docRef = timersCollection.add(data).await()
        Result.success(docRef.id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateTimer(timer: TimerData): Result<Unit> = try {
        val data = hashMapOf(
            "title" to timer.title,
            "totalSeconds" to timer.totalSeconds,
            "remainingSeconds" to timer.remainingSeconds,
            "description" to timer.description,
            "isRunning" to timer.isRunning
        )
        timersCollection.document(timer.id).update(data as Map<String, Any>).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteTimer(timerId: String): Result<Unit> = try {
        timersCollection.document(timerId).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getTimerById(timerId: String): Result<TimerData?> = try {
        val doc = timersCollection.document(timerId).get().await()
        if (doc.exists()) {
            val timer = TimerData(
                id = doc.id,
                title = doc.getString("title") ?: "",
                totalSeconds = doc.getLong("totalSeconds") ?: 0L,
                remainingSeconds = doc.getLong("remainingSeconds") ?: 0L,
                description = doc.getString("description") ?: "",
                isRunning = doc.getBoolean("isRunning") ?: false
            )
            Result.success(timer)
        } else {
            Result.success(null)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getTimersWithSteps(): Result<List<TimerWithSteps>> {
        return Result.success(emptyList())
    }
}
