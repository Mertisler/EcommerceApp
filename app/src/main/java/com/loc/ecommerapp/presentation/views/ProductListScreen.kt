package com.loc.ecommerapp.presentation.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loc.ecommerapp.domain.entities.Product
import com.loc.ecommerapp.presentation.states.ProductListState
import com.loc.ecommerapp.presentation.view_models.ProductListViewModel

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = hiltViewModel(),
    onNavigateToCart: () -> Unit,
    onNavigateToDetail: (Product) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Ekranın sağ alt köşesine "Sepete Git" butonu eklemek için Scaffold kullanıyoruz
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToCart,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text("Sepete Git")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is ProductListState.Loading -> CircularProgressIndicator()

                is ProductListState.Error -> Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )
                is ProductListState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.products) { product ->
                            ProductCard(
                                product = product,
                                // Satın Al yerine sepete ekliyor
                                onAddToCart = { viewModel.addToCart(product) },
                                // Tıklandığında NavGraph üzerinden detay sayfasına yönlendiriyor
                                onProductClick = { onNavigateToDetail(product) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit,
    onProductClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            // Bütün karta tıklanabilirlik özelliği kazandırır
            .clickable { onProductClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Ürün Adı (Varsayılan olarak "name" parametresi olduğunu varsayıyoruz, projenize göre değiştirebilirsiniz)
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Fiyat Bilgisi
            Text(
                text = "Fiyat: ₺${product.price}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Stok Bilgisi
            Text(
                text = "Kalan Stok: ${product.availableStock}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (product.availableStock > 5) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sepete Ekle Butonu
            Button(
                onClick = onAddToCart,
                enabled = product.availableStock > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (product.availableStock > 0) "Sepete Ekle" else "Tükendi")
            }
        }
    }
}