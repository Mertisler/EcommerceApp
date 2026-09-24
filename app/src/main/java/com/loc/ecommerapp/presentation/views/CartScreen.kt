package com.loc.ecommerapp.presentation.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loc.ecommerapp.domain.entities.CartItem
import com.loc.ecommerapp.presentation.view_models.CartViewModel

@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onNavigateToCheckout: () -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text(text = "Sepetim", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Sepetiniz şu an boş.")
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    CartItemRow(
                        item = item,
                        onRemove = { viewModel.removeFromCart(item.productId) }
                    )
                    Divider()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Toplam:", style = MaterialTheme.typography.titleLarge)
                Text(text = "₺$totalAmount", style = MaterialTheme.typography.headlineSmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                onClick = onNavigateToCheckout
            ) {
                Text("Ödemeye Geç")
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onRemove: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.productName, style = MaterialTheme.typography.bodyLarge)
            Text(text = "${item.quantity} Adet", style = MaterialTheme.typography.bodyMedium)
        }
        Text(text = "₺${item.totalPrice}", style = MaterialTheme.typography.titleMedium)
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Delete, contentDescription = "Sil", tint = MaterialTheme.colorScheme.error)
        }
    }
}