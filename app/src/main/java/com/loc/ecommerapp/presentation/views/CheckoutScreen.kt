package com.loc.ecommerapp.presentation.views


import android.R.attr.contentDescription
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loc.ecommerapp.domain.entities.CartItem
import com.loc.ecommerapp.presentation.states.CheckoutState
import com.loc.ecommerapp.presentation.view_models.CheckoutViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle

@Composable
fun CheckoutScreen(
    userId: String,
    cartItems: List<CartItem>,
    totalAmount: Double,
    // Hilt, ViewModel'i ve içine gereken UseCase'i otomatik olarak üretip buraya enjekte eder.
    viewModel: CheckoutViewModel = hiltViewModel(),
    // İşlem başarılı olduğunda NavController ile ana sayfaya dönmek için kullanılacak fonksiyon.
    onNavigateHome: () -> Unit
) {
    // 1. ADIM: StateFlow Dinleniyor. State her değiştiğinde bu Composable otomatik olarak baştan çizilir.
    val uiState by viewModel.uiState.collectAsState()

    // Ekranın temel iskeleti
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Gelen duruma göre (when) ekrana farklı arayüzler (UI) çizilir.
        when (val state = uiState) {

            // DURUM: IDLE (İlk Açılış veya Bekleme)
            is CheckoutState.Idle -> {
                Text(
                    text = "Toplam Tutar: ₺$totalAmount",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    // 2. ADIM: Butona tıklanınca iş mantığı ViewModel'e devredilir
                    onClick = { viewModel.startCheckout(userId, cartItems, totalAmount) }
                ) {
                    Text("Ödemeyi Tamamla")
                }
            }

            // DURUM: LOADING (Stok kilitleniyor ve API isteği atıldı)
            is CheckoutState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Stok ayrılıyor ve ödeme alınıyor...\nLütfen sayfadan ayrılmayın.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // DURUM: SUCCESS (Ödeme alındı, sipariş oluşturuldu)

            is CheckoutState.Success -> {
                Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Başarılı",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Sipariş Başarıyla Oluşturuldu!",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Sipariş No: ${state.order.orderId}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNavigateHome // Başarılı olursa ana sayfaya (veya siparişlerim ekranına) yönlendir
                ) {
                    Text("Ana Sayfaya Dön")
                }
            }

            // DURUM: ERROR (Stok bitti, Bakiye yetersiz, API çöktü vb.)
            is CheckoutState.Error -> {
                Text(
                    text = "İşlem Başarısız",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.message,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    // Hatayı okuduktan sonra durumu sıfırlayıp 'Idle' moduna dön
                    onClick = { viewModel.consumeState() }
                ) {
                    Text("Tekrar Dene")
                }
            }
        }
    }
}