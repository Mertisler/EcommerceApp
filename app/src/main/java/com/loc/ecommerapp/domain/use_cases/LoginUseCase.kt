package com.loc.ecommerapp.domain.use_cases

import com.loc.ecommerapp.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)
}

class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = repository.register(email, password)
}