package com.loc.ecommerapp.data.repositories_impl

import androidx.room.withTransaction // (veya kullanılan ORM'in transaction kütüphanesi)
import com.loc.ecommerapp.data.api.PaymentApi
import com.loc.ecommerapp.data.api.models.PaymentRequestDto
import com.loc.ecommerapp.data.local.OrderDao
import com.loc.ecommerapp.domain.entities.CartItem
import com.loc.ecommerapp.domain.entities.Order
import com.loc.ecommerapp.domain.repositories.OrderRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val paymentApi: PaymentApi,
    private val database: AppDatabase // Transaction işlemleri için gerekli
) : OrderRepository {

    override suspend fun reserveStock(items: List<CartItem>): Result<Unit> {
        return try {
            // Veritabanı Transaction bloğu başlatılır (Atomik işlem)
            database.withTransaction {
                for (item in items) {
                    // 1. Satırı kilitle ve güncel stoğu al (Pessimistic Lock)
                    val product = orderDao.getProductForUpdate(item.productId)
                        ?: throw Exception("${item.productName} bulunamadı.")

                    // 2. İş Kuralı Kontrolü
                    if (product.actualStock < item.quantity) {
                        // Eğer tek bir ürünün bile stoğu yetmezse hata fırlatılır.
                        // Hata fırladığı an 'withTransaction' bloğu iptal olur ve
                        // önceki döngülerde yapılan tüm update'ler otomatik geri alınır!
                        throw Exception("${item.productName} için stok yetersiz.")
                    }

                    // 3. Rezervasyonu gerçekleştir
                    orderDao.reserveStock(item.productId, item.quantity)
                }
            }
            // Transaction başarıyla biterse:
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun processPayment(amount: Double): Result<String> {
        return try {
            // API'nin beklediği DTO formatını (Data Transfer Object) hazırla
            val requestDto = PaymentRequestDto(
                totalAmount = amount,
                timestamp = System.currentTimeMillis() // Geçerlilik zaman damgası
            )

            // Ağ isteğini fırlat
            val response = paymentApi.processPayment(requestDto)

            // Yanıtı değerlendir
            if (response.isSuccessful && response.body()?.statusCode == 200) {
                val transactionId = response.body()?.transactionId ?: "TRX_UNKNOWN"
                Result.success(transactionId)
            } else {
                val errorMsg = response.body()?.errorMessage ?: "Bilinmeyen API Hatası"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            // İnternet kesintisi, Timeout gibi ağ hataları
            Result.failure(Exception("Ödeme API'sine ulaşılamadı: ${e.message}"))
        }
    }

    override suspend fun commitOrder(order: Order) {
        // Sepetteki ürünlerin "Rezerve" stoklarını kalıcı olarak siler
        database.withTransaction {
            for (item in order.items) {
                orderDao.commitStock(item.productId, item.quantity)
            }
            // İsteğe bağlı: Sipariş geçmişi (History) tablosuna order objesi kaydedilebilir.
        }
    }

    override suspend fun rollbackReservation(items: List<CartItem>) {
        // İptal edilen sepetteki ürünleri rezerve stoktan çıkarıp gerçek stoğa geri ekler
        database.withTransaction {
            for (item in items) {
                orderDao.rollbackStock(item.productId, item.quantity)
            }
        }
    }
}