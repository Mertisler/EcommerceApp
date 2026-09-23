package com.loc.ecommerapp.presentation.states

import com.loc.ecommerapp.domain.entities.Order


sealed interface CheckoutState {
    // Ekrana ilk girildiğinde veya işlem sıfırlandığındaki boş durum
    object Idle : CheckoutState

    // İşlem devam ederken (Stok kilitlenirken veya ödeme çekilirken)
    object Loading : CheckoutState

    // İşlem başarılı bittiğinde fırlatılacak durum (İçinde sipariş detayını barındırır)
    data class Success(val order: Order) : CheckoutState

    // Stok yetersizliği, API çökmesi veya bakiye yetersizliğinde fırlatılacak durum
    data class Error(val message: String) : CheckoutState
}