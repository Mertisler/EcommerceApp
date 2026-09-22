package com.loc.ecommerapp.domain.entities

data class CartItem(
    val productId: String,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int
) {
    // İş Kuralı: Bu kalemin toplam tutarı hesaplanarak döndürülür
    val totalPrice: Double
        get() = unitPrice * quantity

    // İş Kuralı: Miktar artırıldığında veya azaltıldığında yeni bir kopyasını döner (Immutable yaklaşım)
    fun updateQuantity(newQuantity: Int): CartItem {
        require(newQuantity > 0) { "Miktar 0 veya negatif olamaz" }
        return this.copy(quantity = newQuantity)
    }
}