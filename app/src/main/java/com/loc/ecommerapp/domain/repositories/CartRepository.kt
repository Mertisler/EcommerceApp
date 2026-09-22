package com.loc.ecommerapp.domain.repositories

import com.loc.ecommerapp.domain.entities.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    // Flow kullanılmasının sebebi, sepetteki değişikliklerin anlık olarak (Reactive) UI'a yansımasını sağlamaktır.
    fun getCartItems(): Flow<List<CartItem>>

    // Sepete yeni ürün ekler veya var olan ürünün miktarını artırır
    suspend fun addToCart(item: CartItem)

    // Sepetten ürünü tamamen çıkarır
    suspend fun removeFromCart(productId: String)

    // Sepeti tamamen boşaltır (Sipariş başarıyla tamamlandığında çağrılacak)
    suspend fun clearCart()
}