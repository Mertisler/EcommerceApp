package com.loc.ecommerapp.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.ecommerapp.domain.entities.CartItem
import com.loc.ecommerapp.domain.use_cases.ProcessCheckoutUseCase
import com.loc.ecommerapp.presentation.states.CheckoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    // ViewModel sadece UseCase'i bilir. Repository, API, veya Dao bilmez! (Clean Architecture)
    private val processCheckoutUseCase: ProcessCheckoutUseCase
) : ViewModel() {

    // Kapsülleme (Encapsulation): _uiState sadece içeriden değiştirilebilir.
    private val _uiState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)

    // uiState (Dışarıya açık): View (Compose) tarafından 'collectAsState' ile dinlenir, değiştirilemez.
    val uiState: StateFlow<CheckoutState> = _uiState.asStateFlow()

    fun startCheckout(userId: String, cartItems: List<CartItem>, totalAmount: Double) {
        // Eğer şu an zaten bir ödeme işlemi sürüyorsa (Butona çift tıklandıysa) yoksay.
        if (_uiState.value is CheckoutState.Loading) return

        // 1. Ekranı yükleme durumuna geçir
        _uiState.value = CheckoutState.Loading

        // 2. Coroutine içinde (Asenkron) iş mantığını (UseCase) başlat
        viewModelScope.launch {

            // UseCase çalışır (arkaplanda reserve, payment, commit/rollback yapar)
            val result = processCheckoutUseCase(
                userId = userId,
                cartItems = cartItems,
                totalAmount = totalAmount
            )

            // 3. Sonuca göre UI durumunu güncelle
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

    // İşlem bittiğinde (Örn: Hata dialogu kapatıldığında) ekranı başa döndürmek için
    fun consumeState() {
        _uiState.value = CheckoutState.Idle
    }
}