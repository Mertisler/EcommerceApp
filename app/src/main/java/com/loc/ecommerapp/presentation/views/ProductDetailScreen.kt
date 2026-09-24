package com.loc.ecommerapp.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loc.ecommerapp.presentation.states.ProductDetailState
import com.loc.ecommerapp.presentation.view_models.ProductDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ürün Detayı") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is ProductDetailState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProductDetailState.Error -> {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadProductDetail() }) {
                            Text("Tekrar Dene")
                        }
                    }
                }
                is ProductDetailState.Success -> {
                    val product = state.product

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 80.dp) // Alt bar için boşluk
                    ) {
                        // Ürün Görseli Yer Tutucu (İleride Coil kütüphanesi ile URL'den resim basılır)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Ürün Görseli", color = Color.DarkGray)
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "₺${product.price}",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Divider()
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Ürün Açıklaması",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = product.description,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    // Yapışkan Alt Bar (Sepete Ekle Butonu)
                    Surface(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Toplam Fiyat", style = MaterialTheme.typography.bodySmall)
                                Text(
                                    "₺${product.price}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = {
                                    viewModel.addToCart(product) {
                                        onNavigateToCart()
                                    }
                                },
                                enabled = product.availableStock > 0,
                                modifier = Modifier.height(50.dp)
                            ) {
                                Text(if (product.availableStock > 0) "Sepete Ekle" else "Tükendi")
                            }
                        }
                    }
                }
            }
        }
    }
}