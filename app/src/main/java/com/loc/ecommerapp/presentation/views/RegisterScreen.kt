package com.loc.ecommerapp.presentation.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loc.ecommerapp.presentation.states.AuthState
import com.loc.ecommerapp.presentation.view_models.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val uiState by viewModel.uiState.collectAsState()

    // Başarılı kayıtta doğrudan ana sayfaya yönlendir
    LaunchedEffect(uiState) {
        if (uiState is AuthState.Success) {
            onNavigateToHome()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Yeni Hesap Oluştur", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-posta") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Şifre") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                passwordError = if (password != it) "Şifreler eşleşmiyor" else null
            },
            label = { Text("Şifreyi Onayla") },
            isError = passwordError != null,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        if (passwordError != null) {
            Text(passwordError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState is AuthState.Error) {
            Text(
                text = (uiState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                if (password == confirmPassword) {
                    viewModel.register(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = uiState !is AuthState.Loading && password == confirmPassword && password.isNotBlank()
        ) {
            if (uiState is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Kayıt Ol")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            viewModel.resetState()
            onNavigateBackToLogin()
        }) {
            Text("Zaten hesabınız var mı? Giriş Yapın")
        }
    }
}