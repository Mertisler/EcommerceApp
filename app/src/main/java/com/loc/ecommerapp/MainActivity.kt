package com.loc.ecommerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.loc.ecommerapp.domain.entities.CartItem
import com.loc.ecommerapp.presentation.navigation.EcommerceNavGraph
import com.loc.ecommerapp.presentation.views.CheckoutScreen
import com.loc.ecommerapp.ui.theme.EcommerappTheme
import dagger.hilt.android.AndroidEntryPoint

// Hilt'in bu Activity'yi tanıması ve ViewModel enjeksiyonunu yapabilmesi için zorunludur.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ekranı test edebilmek için örnek (Mock) sipariş verileri
        val mockCartItems = listOf(
            CartItem(
                productId = "P1",
                productName = "Kablosuz Kulaklık",
                unitPrice = 1500.0,
                quantity = 1
            ),
            CartItem(
                productId = "P2",
                productName = "Akıllı Saat",
                unitPrice = 3000.0,
                quantity = 1
            )
        )
        val mockTotalAmount = 4500.0
        val mockUserId = "USER_123"

        // MainActivity.kt içindeki setContent bloğu:
        setContent {
            EcommerappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        // Artık tek bir ekranı değil, tüm yönlendirme ağını çağırıyoruz
                        EcommerceNavGraph()
                    }
                }
            }
        }
    }
}