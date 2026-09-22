package com.loc.ecommerapp.domain.entities

import java.util.Date

data class Order(
    val orderId: String,
    val userId: String,
    val items: List<CartItem>,
    val status: OrderStatus,
    val createdAt: Date
) {
    // İş Kuralı: Sepetteki tüm kalemlerin toplam tutarını hesaplar
    val totalAmount: Double
        get() = items.sumOf { it.totalPrice }
}