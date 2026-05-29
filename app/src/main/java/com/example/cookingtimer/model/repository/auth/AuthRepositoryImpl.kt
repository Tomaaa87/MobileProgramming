package com.example.cookingtimer.model.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.cookingtimer.model.repository.SessionManager
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager
) : AuthRepository {

    init {
        // Handle persistent login: check if user is already signed in
        firebaseAuth.currentUser?.let {
            sessionManager.login(it.uid)
        }
    }

    override suspend fun login(email: String, password: String): Result<Boolean> = try {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        result.user?.let {
            sessionManager.login(it.uid)
            Result.success(true)
        } ?: Result.failure(Exception("Login failed: User is null"))
    } catch (e: Exception) {
        Result.failure(mapFirebaseError(e))
    }

    override suspend fun register(fullName: String, email: String, password: String): Result<Unit> = try {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        result.user?.let { user ->
            // Create user profile in Firestore
            val profile = hashMapOf(
                "displayName" to fullName,
                "email" to email,
                "bio" to "Cooking enthusiast",
                "notificationsEnabled" to true,
                "emailUpdates" to false,
                "selectedTheme" to "System",
                "fontSize" to 16.0,
                "achievements" to emptyList<String>()
            )
            firestore.collection("users").document(user.uid).set(profile).await()
            
            sessionManager.login(user.uid)
            Result.success(Unit)
        } ?: Result.failure(Exception("Registration failed: User is null"))
    } catch (e: Exception) {
        Result.failure(mapFirebaseError(e))
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
        sessionManager.logout()
    }

    private fun mapFirebaseError(e: Exception): Exception {
        return Exception(e.localizedMessage ?: "An authentication error occurred")
    }
}
