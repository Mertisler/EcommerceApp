package com.loc.ecommerapp.domain.repositories

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String> // Başarılıysa Kullanıcı ID (UID) döner
    suspend fun register(email: String, password: String): Result<String>
    fun getCurrentUserId(): String?
    fun logout()
}