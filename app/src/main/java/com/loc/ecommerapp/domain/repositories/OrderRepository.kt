package com.loc.ecommerapp.domain.repositories

import com.loc.ecommerapp.domain.entities.CartItem
import com.loc.ecommerapp.domain.entities.Order

interface OrderRepository {

    /**
     * Adım 1: Sipariş edilmek istenen ürünlerin stoğunu geçici olarak kilitler/rezerve eder.
     * @return Başarılıysa Unit, stok yetersizse veya kilitlenemezse Result.failure döner.
     */
    suspend fun reserveStock(items: List<CartItem>): Result<Unit>

    /**
     * Adım 2: Toplam tutarı ödeme sağlayıcısına gönderir.
     * @return Ödeme başarılıysa onay kodunu (String), başarısızsa hata bilgisini döner.
     */
    suspend fun processPayment(amount: Double): Result<String>

    /**
     * Adım 3 (Başarılı Senaryo): Rezerve edilen stoğu kalıcı olarak düşer ve siparişi onaylar.
     */
    suspend fun commitOrder(order: Order)

    /**
     * Adım 4 (Hata/İptal Senaryosu): Ayrılan stokları (Rezervasyonu) serbest bırakıp sisteme geri ekler.
     */
    suspend fun rollbackReservation(items: List<CartItem>)
}