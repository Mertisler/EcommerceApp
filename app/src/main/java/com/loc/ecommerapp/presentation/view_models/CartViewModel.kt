package com.loc.ecommerapp.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.ecommerapp.domain.entities.CartItem
import com.loc.ecommerapp.domain.repositories.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    // Sepetteki ürünleri anlık olarak dinler
    val cartItems = cartRepository.getCartItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Sepetteki ürünler değiştikçe toplam fiyatı otomatik olarak yeniden hesaplar
    val totalAmount = cartItems.map { items ->
        items.sumOf { it.totalPrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            cartRepository.removeFromCart(productId)
        }
    }

    // Not: İleride buraya increaseQuantity, decreaseQuantity gibi fonksiyonlar eklenebilir.
}