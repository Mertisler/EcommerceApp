package com.loc.ecommerapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "actual_stock") val actualStock: Int,
    @ColumnInfo(name = "reserved_stock") val reservedStock: Int // Rezerve edilen miktarı takip etmek için
)