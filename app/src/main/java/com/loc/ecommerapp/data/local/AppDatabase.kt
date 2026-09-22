package com.loc.ecommerapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.loc.ecommerapp.data.local.entities.ProductEntity

// 1. Database Anotasyonu: Veritabanındaki tüm tablolar (entities) buraya dizi olarak eklenir.
@Database(
    entities = [ProductEntity::class], // İleride OrderEntity, CartEntity vb. ekleyebilirsiniz
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // 2. DAO Erişimleri: Dış dünyadan DAO'lara erişmek için soyut fonksiyonlar tanımlanır.
    // OrderRepositoryImpl bu fonksiyon üzerinden orderDao'yu çağırır.
    abstract fun orderDao(): OrderDao

    /*
     * DİKKAT: Eski projelerde burada 'Companion Object' içinde
     * "fun getInstance(context: Context)" adında karmaşık bir Singleton yapısı görürsünüz.
     * Ancak Hilt (Dependency Injection) kullandığımız için o kalabalık koda İHTİYACIMIZ YOKTUR.
     * Veritabanının oluşturulma işini Hilt Module katmanına devredeceğiz.
     */
}