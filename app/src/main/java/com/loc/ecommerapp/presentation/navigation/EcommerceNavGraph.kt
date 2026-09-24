package com.loc.ecommerapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.loc.ecommerapp.presentation.views.CartScreen
import com.loc.ecommerapp.presentation.views.CheckoutScreen
import com.loc.ecommerapp.presentation.views.ProductDetailScreen
import com.loc.ecommerapp.presentation.views.ProductListScreen

@Composable
fun EcommerceNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "product_list"
    ) {

        // 1. EKRAN: Ürün Listesi
        composable("product_list") {
            ProductListScreen(
                onNavigateToCart = { navController.navigate("cart") },
                onNavigateToDetail = { selectedProduct ->
                    navController.navigate("product_detail/${selectedProduct.id}")
                }
            )
        }

        // 2. EKRAN: Sepet Sayfası
        composable("cart") {
            CartScreen(
                onNavigateToCheckout = {
                    navController.navigate("checkout")
                }
            )
        }

        // 3. EKRAN: Ödeme Sayfası
        composable("checkout") {
            CheckoutScreen(
                userId = "USER_123", // Gerçek projede login'den gelir
                // cartItems ve totalAmount sildik, sayfa kendini yönetecek
                onNavigateHome = {
                    navController.popBackStack(
                        route = "product_list",
                        inclusive = false
                    )
                }
            )
        }

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