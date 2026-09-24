package com.loc.ecommerapp.presentation.views

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
import com.loc.ecommerapp.presentation.states.CheckoutState
import com.loc.ecommerapp.presentation.view_models.CheckoutViewModel

@Composable
fun CheckoutScreen(
    userId: String,
    viewModel: CheckoutViewModel = hiltViewModel(),
    onNavigateHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // EKLENDİ: Toplam tutarı ViewModel'den dinliyoruz
    val totalAmount by viewModel.totalAmount.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = uiState) {
            is CheckoutState.Idle -> {
                // ViewModel'den gelen güncel fiyat yazdırılır
                Text(
                    text = "Toplam Tutar: ₺$totalAmount",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    // DEĞİŞTİRİLDİ: Sadece userId gönderiyoruz
                    onClick = { viewModel.startCheckout(userId) }
                ) {
                    Text("Ödemeyi Tamamla")
                }
            }
            is CheckoutState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Stok ayrılıyor ve ödeme alınıyor...\nLütfen sayfadan ayrılmayın.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            is CheckoutState.Success -> {
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
                    onClick = onNavigateHome
                ) {
                    Text("Alışverişe Devam Et")
                }
            }
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
                    onClick = { viewModel.consumeState() }
                ) {
                    Text("Tekrar Dene")
                }
            }
        }
    }
}