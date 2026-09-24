package com.loc.ecommerapp.data.repositories_impl

import com.loc.ecommerapp.domain.entities.Product
import com.loc.ecommerapp.domain.repositories.ProductRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor() : ProductRepository {

    // Verilerin her iki fonksiyondan da erişilebilmesi için sınıf seviyesine alındı
    private val mockData = listOf(
        Product("P1", "MacBook Pro", 85000.0, 5, "M3 Çip, 16GB RAM"),
        Product("P2", "Logitech MX Master 3", 3500.0, 12, "Kablosuz Mouse"),
        Product("P3", "Mekanik Klavye", 2000.0, 0, "Stokta yok") // Tükenmiş ürün
    )

    override suspend fun getProducts(): Result<List<Product>> {
        // API isteği gibi davranması için 1.5 saniye gecikme
        delay(1500)
        return Result.success(mockData)
    }

    // Arayüzün (Interface) zorunlu kıldığı eksik fonksiyon eklendi
    override suspend fun getProductById(id: String): Result<Product> { // Entity'nizdeki ID parametresinin ismine göre düzeltin
        // Detay isteği için daha kısa bir ağ gecikmesi simülasyonu
        delay(500)

        // Gönderilen ID ile eşleşen ürünü listede arıyoruz
        val product = mockData.find { it.id == id }

        return if (product != null) {
            Result.success(product)
        } else {
            Result.failure(Exception("Aradığınız ürün bulunamadı."))
        }
    }
}