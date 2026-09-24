package com.loc.ecommerapp.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.ecommerapp.domain.repositories.CartRepository
import com.loc.ecommerapp.domain.use_cases.ProcessCheckoutUseCase
import com.loc.ecommerapp.presentation.states.CheckoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val processCheckoutUseCase: ProcessCheckoutUseCase,
    private val cartRepository: CartRepository // EKLENDİ: Sepet verisini almak için
) : ViewModel() {

    private val _uiState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    val uiState: StateFlow<CheckoutState> = _uiState.asStateFlow()

    // EKLENDİ: Ekranda toplam tutarı göstermek için sepeti dinliyoruz
    val totalAmount: StateFlow<Double> = cartRepository.getCartItems()
        .map { items -> items.sumOf { it.totalPrice } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // DEĞİŞTİRİLDİ: Artık UI'dan cartItems ve totalAmount beklemiyoruz
    fun startCheckout(userId: String) {
        if (_uiState.value is CheckoutState.Loading) return

        _uiState.value = CheckoutState.Loading

        viewModelScope.launch {
            // 1. Sepetin o anki güncel halini Repository'den çekiyoruz
            val currentCartItems = cartRepository.getCartItems().first()
            val currentTotalAmount = currentCartItems.sumOf { it.totalPrice }

            // 2. UseCase'i bu gerçek verilerle başlatıyoruz
            val result = processCheckoutUseCase(
                userId = userId,
                cartItems = currentCartItems,
                totalAmount = currentTotalAmount
            )

            result.fold(
                onSuccess = { createdOrder ->
                    _uiState.value = CheckoutState.Success(createdOrder)
                },
                onFailure = { exception ->
                    val errorMsg = exception.localizedMessage ?: "Bilinmeyen bir hata oluştu."
                    _uiState.value = CheckoutState.Error(errorMsg)
                }
            )
        }
    }

    fun consumeState() {
        _uiState.value = CheckoutState.Idle
    }
}