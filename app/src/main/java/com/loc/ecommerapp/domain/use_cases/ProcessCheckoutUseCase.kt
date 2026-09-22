package com.loc.ecommerapp.domain.use_cases

import com.loc.ecommerapp.domain.entities.CartItem
import com.loc.ecommerapp.domain.entities.Order
import com.loc.ecommerapp.domain.entities.OrderStatus
import com.loc.ecommerapp.domain.repositories.CartRepository
import com.loc.ecommerapp.domain.repositories.OrderRepository
import java.util.Date
import java.util.UUID

class ProcessCheckoutUseCase(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository
) {
    /**
     * operator fun invoke: Bu sınıfın bir fonksiyon gibi (processCheckoutUseCase())
     * doğrudan çağrılabilmesini sağlar.
     */
    suspend operator fun invoke(
        userId: String,
        cartItems: List<CartItem>,
        totalAmount: Double
    ): Result<Order> {

        // İş Kuralı 1: Sepet boşsa işlemi başlatma
        if (cartItems.isEmpty()) {
            return Result.failure(Exception("Sepetiniz boş."))
        }

        return try {
            // İş Kuralı 2: Önce stok rezervasyonunu (Kilit) dene
            val reserveResult = orderRepository.reserveStock(cartItems)

            if (reserveResult.isFailure) {
                // Stok ayrılamadı (Başka biri aldı veya tükendi), işlemi kes.
                return Result.failure(Exception("Stok yetersiz veya ürünler tükendi."))
            }

            // İş Kuralı 3: Stok ayrıldı, ödeme işlemini başlat
            val paymentResult = orderRepository.processPayment(totalAmount)

            if (paymentResult.isSuccess) {
                // Başarılı Senaryo: Sipariş modelini oluştur (Durum: COMPLETED)
                val newOrder = Order(
                    orderId = UUID.randomUUID().toString(),
                    userId = userId,
                    items = cartItems,
                    status = OrderStatus.COMPLETED,
                    createdAt = Date()
                )

                // Stoğu kalıcı düş ve siparişi kaydet (Commit)
                orderRepository.commitOrder(newOrder)

                // Sipariş onaylandığı için kullanıcının sepetini temizle
                cartRepository.clearCart()

                Result.success(newOrder)
            } else {
                // Hata Senaryosu 1: Ödeme reddedildi, kilitli stokları serbest bırak (Rollback)
                orderRepository.rollbackReservation(cartItems)

                val errorMessage = paymentResult.exceptionOrNull()?.message ?: "Ödeme reddedildi."
                Result.failure(Exception(errorMessage))
            }

        } catch (e: Exception) {
            // Hata Senaryosu 2: Sunucu çöktü, internet koptu veya beklenmeyen yazılım hatası.
            // Transaction havada kalmasın diye (Deadlock önleme) stokları acil serbest bırak.
            orderRepository.rollbackReservation(cartItems)
            Result.failure(Exception("Beklenmeyen bir hata oluştu: ${e.localizedMessage}"))
        }
    }
}