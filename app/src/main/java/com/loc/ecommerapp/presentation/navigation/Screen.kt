package com.loc.ecommerapp.presentation.navigation

import android.net.Uri

sealed class Screen(val route: String) {

    // Ana sayfa sabit
    object ProductList : Screen("product_list")

    // Argümanlı Checkout Rotası: Zorunlu argüman ({productId}), İsteğe bağlı argümanlar (?name=...&price=...)
    object Checkout : Screen("checkout/{productId}?name={productName}&price={price}") {

        // ProductList'ten gönderim yaparken adresi kolayca oluşturmak için yardımcı fonksiyon
        fun createRoute(productId: String, productName: String, price: Double): String {
            // İsimde boşluk (MacBook Pro) olabileceği için URL'yi bozmamasını sağlıyoruz
            val encodedName = Uri.encode(productName)
            return "checkout/$productId?name=$encodedName&price=$price"
        }
    }
}