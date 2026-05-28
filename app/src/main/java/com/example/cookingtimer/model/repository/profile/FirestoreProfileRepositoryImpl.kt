package com.example.cookingtimer.model.repository.profile

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.example.cookingtimer.model.repository.SessionManager
import com.example.cookingtimer.presentation.view_model.profile.ProfileData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class FirestoreProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager
) : ProfileRepository {

    private val usersCollection = firestore.collection("users")

    override fun getProfileDataFlow(): Flow<ProfileData?> {
        return sessionManager.currentUserId.flatMapLatest { userId ->
            if (userId.isNullOrEmpty()) {
                flowOf(null)
            } else {
                usersCollection.document(userId).snapshots().map { doc ->
                    if (doc.exists()) {
                        ProfileData(
                            displayName = doc.getString("displayName") ?: "User",
                            email = doc.getString("email") ?: "",
                            bio = doc.getString("bio") ?: "",
                            notificationsEnabled = doc.getBoolean("notificationsEnabled") ?: true,
                            emailUpdates = doc.getBoolean("emailUpdates") ?: false,
                            selectedTheme = doc.getString("selectedTheme") ?: "System",
                            fontSize = doc.getDouble("fontSize")?.toFloat() ?: 16f,
                            achievements = doc.get("achievements") as? List<String> ?: emptyList()
                        )
                    } else {
                        null
                    }
                }
            }
        }
    }

    override suspend fun getProfileData(): Result<ProfileData> = try {
        val userId = sessionManager.getUserId()
        if (userId.isEmpty()) {
            Result.failure(Exception("Not logged in"))
        } else {
            val doc = usersCollection.document(userId).get().await()
            if (doc.exists()) {
                val profile = ProfileData(
                    displayName = doc.getString("displayName") ?: "User",
                    email = doc.getString("email") ?: "",
                    bio = doc.getString("bio") ?: "",
                    notificationsEnabled = doc.getBoolean("notificationsEnabled") ?: true,
                    emailUpdates = doc.getBoolean("emailUpdates") ?: false,
                    selectedTheme = doc.getString("selectedTheme") ?: "System",
                    fontSize = doc.getDouble("fontSize")?.toFloat() ?: 16f,
                    achievements = doc.get("achievements") as? List<String> ?: emptyList()
                )
                Result.success(profile)
            } else {
                Result.failure(Exception("Profile not found"))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateProfileData(data: ProfileData): Result<Unit> = try {
        val userId = sessionManager.getUserId()
        if (userId.isEmpty()) {
            Result.failure(Exception("Not logged in"))
        } else {
            val profileMap = hashMapOf(
                "displayName" to data.displayName,
                "email" to data.email,
                "bio" to data.bio,
                "notificationsEnabled" to data.notificationsEnabled,
                "emailUpdates" to data.emailUpdates,
                "selectedTheme" to data.selectedTheme,
                "fontSize" to data.fontSize.toDouble(),
                "achievements" to data.achievements
            )
            usersCollection.document(userId).set(profileMap).await()
            Result.success(Unit)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
