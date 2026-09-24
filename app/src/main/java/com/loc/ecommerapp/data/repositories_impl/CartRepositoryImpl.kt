package com.loc.ecommerapp.data.repositories_impl

import com.loc.ecommerapp.domain.entities.CartItem
import com.loc.ecommerapp.domain.repositories.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// @Inject constructor Hilt'in bu sınıfı otomatik oluşturabilmesi için şarttır
class CartRepositoryImpl @Inject constructor() : CartRepository {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())

    override fun getCartItems(): Flow<List<CartItem>> {
        return _cartItems.asStateFlow()
    }

    override suspend fun addToCart(item: CartItem) {
        _cartItems.update { currentList ->
            val mutableList = currentList.toMutableList()
            mutableList.add(item)
            mutableList
        }
    }

    override suspend fun removeFromCart(productId: String) {
        _cartItems.update { currentList ->
            currentList.filter { it.productId != productId }
        }
    }

    override suspend fun clearCart() {
        _cartItems.value = emptyList()
    }
}