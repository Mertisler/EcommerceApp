package com.loc.ecommerapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.loc.ecommerapp.presentation.views.*

@Composable
fun EcommerceNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val firebaseAuth = FirebaseAuth.getInstance()
    // Oturum durumuna göre dinamik başlangıç rotası
    val startDestination = if (firebaseAuth.currentUser != null) "login" else "product_list"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // 1. GİRİŞ SAYFASI
        composable("login") {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate("product_list") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        // 2. KAYIT SAYFASI
        composable("register") {
            RegisterScreen(
                onNavigateToHome = {
                    navController.navigate("product_list") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("product_list") {
            ProductListScreen(
                onNavigateToCart = { navController.navigate("cart") },
                onNavigateToDetail = { selectedProduct ->
                    navController.navigate("product_detail/${selectedProduct.id}")
                }
            )
        }

        composable("cart") {
            CartScreen(
                onNavigateToCheckout = {
                    navController.navigate("checkout")
                }
            )
        }

        // 3. ÖDEME SAYFASI (Gerçek Kullanıcı Kimliği ile)
        composable("checkout") {
            val currentUserId = firebaseAuth.currentUser?.uid ?: "BİLİNMEYEN_KULLANICI"

            CheckoutScreen(
                userId = currentUserId,
                onNavigateHome = {
                    navController.popBackStack(
                        route = "product_list",
                        inclusive = false
                    )
                }
            )
        }

        // 4. ÜRÜN DETAY SAYFASI
        composable(
            route = "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) {
            ProductDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCart = { navController.navigate("cart") }
            )
        }
    }
}