package com.loc.ecommerapp.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.loc.ecommerapp.data.local.AppDatabase
import com.loc.ecommerapp.data.local.OrderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ecommerce_db"
        )
            // Veritabanı ilk kez oluşturulurken tetiklenecek Callback
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Başlangıç ürünlerini ham SQL ile tabloya ekliyoruz
                    db.execSQL("INSERT INTO products (id, name, price, actual_stock, reserved_stock) VALUES ('P1', 'MacBook Pro', 85000.0, 5, 0)")
                    db.execSQL("INSERT INTO products (id, name, price, actual_stock, reserved_stock) VALUES ('P2', 'Logitech MX Master 3', 3500.0, 12, 0)")
                    db.execSQL("INSERT INTO products (id, name, price, actual_stock, reserved_stock) VALUES ('P3', 'Mekanik Klavye', 2000.0, 0, 0)")
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideOrderDao(database: AppDatabase): OrderDao {
        return database.orderDao()
    }
}