package com.loc.ecommerapp.domain.entities

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val availableStock: Int,
    val description: String
) {
    // İş Kuralı: İstenen miktar mevcut stoktan az veya eşit mi?
    fun hasSufficientStock(requestedQuantity: Int): Boolean {
        return availableStock >= requestedQuantity
    }
}
