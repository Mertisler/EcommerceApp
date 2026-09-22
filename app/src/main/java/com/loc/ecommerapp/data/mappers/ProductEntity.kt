package com.loc.ecommerapp.data.mappers

import com.loc.ecommerapp.data.local.entities.ProductEntity
import com.loc.ecommerapp.domain.entities.Product

// Veritabanı tablosundan (Entity), saf iş modeline (Domain Model) dönüşüm
fun ProductEntity.toDomainModel(): Product {
    return Product(
        id = this.id,
        name = this.name,
        price = this.price,
        // İş mantığı gereği kullanılabilir stok = Gerçek stok (rezerve edilmemiş kısım)
        availableStock = this.actualStock,
        description = "Açıklama veritabanından alınabilir"
    )
}