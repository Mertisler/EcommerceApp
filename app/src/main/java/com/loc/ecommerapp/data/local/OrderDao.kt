package com.loc.ecommerapp.data.local

import androidx.room.Dao
import androidx.room.Query
import com.loc.ecommerapp.data.local.entities.ProductEntity


@Dao
interface OrderDao {

    // Kilitli Okuma (Pessimistic Lock)
    // Sadece mevcut transaction bitene kadar diğer işlemlerin bu satırı okumasını/yazmasını engeller
    // Düzeltilmiş Hali:
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductForUpdate(productId: String): ProductEntity?

    // Stok Rezervasyonu: İlgili miktar gerçek stoktan düşülür, rezerve stoğa eklenir
    @Query("""
        UPDATE products 
        SET actual_stock = actual_stock - :quantity, 
            reserved_stock = reserved_stock + :quantity 
        WHERE id = :productId AND actual_stock >= :quantity
    """)
    suspend fun reserveStock(productId: String, quantity: Int): Int // Etkilenen satır sayısını döner

    // Başarılı Senaryo (Commit): Rezerve stoğu tamamen siler
    @Query("""
        UPDATE products 
        SET reserved_stock = reserved_stock - :quantity 
        WHERE id = :productId
    """)
    suspend fun commitStock(productId: String, quantity: Int)

    // Hata Senaryosu (Rollback): Rezerve stoğu iptal edip gerçek stoğa geri yükler
    @Query("""
        UPDATE products 
        SET actual_stock = actual_stock + :quantity, 
            reserved_stock = reserved_stock - :quantity 
        WHERE id = :productId
    """)
    suspend fun rollbackStock(productId: String, quantity: Int)
}