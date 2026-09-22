package com.loc.ecommerapp.domain.entities

enum class OrderStatus {
    PENDING,          // Başlangıç durumu, sepet onaylandı
    STOCK_RESERVED,   // Stok kilitlendi, ödeme bekleniyor
    COMPLETED,        // Ödeme onaylandı, stok kalıcı düşüldü
    CANCELLED         // Hata, zaman aşımı veya kullanıcı iptali (Rollback)
}