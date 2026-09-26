package com.loc.ecommerapp.data.repositories_impl

import com.google.firebase.auth.FirebaseAuth
import com.loc.ecommerapp.domain.repositories.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Kullanıcı kimliği alınamadı")
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String): Result<String> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Kayıt oluşturulamadı")
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}