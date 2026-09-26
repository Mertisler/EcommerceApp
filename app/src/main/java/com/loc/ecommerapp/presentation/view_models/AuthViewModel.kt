package com.loc.ecommerapp.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.ecommerapp.domain.use_cases.LoginUseCase
import com.loc.ecommerapp.domain.use_cases.RegisterUseCase
import com.loc.ecommerapp.presentation.states.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthState>(AuthState.Idle)
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthState.Error("E-posta ve şifre boş olamaz.")
            return
        }

        _uiState.value = AuthState.Loading
        viewModelScope.launch {
            loginUseCase(email, password).fold(
                onSuccess = { uid -> _uiState.value = AuthState.Success(uid) },
                onFailure = { e -> _uiState.value = AuthState.Error(e.localizedMessage ?: "Giriş başarısız") }
            )
        }
    }

    fun register(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthState.Error("E-posta ve şifre boş olamaz.")
            return
        }

        _uiState.value = AuthState.Loading
        viewModelScope.launch {
            registerUseCase(email, password).fold(
                onSuccess = { uid -> _uiState.value = AuthState.Success(uid) },
                onFailure = { e -> _uiState.value = AuthState.Error(e.localizedMessage ?: "Kayıt başarısız") }
            )
        }
    }

    fun resetState() {
        _uiState.value = AuthState.Idle
    }
}