package com.loc.ecommerapp.presentation.view_models

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.ecommerapp.domain.entities.Product
import com.loc.ecommerapp.domain.use_cases.AddToCartUseCase
import com.loc.ecommerapp.domain.use_cases.GetProductDetailUseCase
import com.loc.ecommerapp.presentation.states.ProductDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    savedStateHandle: SavedStateHandle // URL'den parametreleri otomatik yakalar
) : ViewModel() {

    // NavGraph'ta tanımlayacağımız "productId" argümanını alıyoruz
    private val productId: String = checkNotNull(savedStateHandle["productId"])

    private val _uiState = MutableStateFlow<ProductDetailState>(ProductDetailState.Loading)
    val uiState: StateFlow<ProductDetailState> = _uiState.asStateFlow()

    init {
        loadProductDetail()
    }

    fun loadProductDetail() {
        _uiState.value = ProductDetailState.Loading
        viewModelScope.launch {
            val result = getProductDetailUseCase(productId)
            result.fold(
                onSuccess = { product ->
                    _uiState.value = ProductDetailState.Success(product)
                },
                onFailure = {
                    _uiState.value = ProductDetailState.Error("Ürün detayları yüklenemedi.")
                }
            )
        }
    }

    fun addToCart(product: Product, onSuccess: () -> Unit) {
        viewModelScope.launch {
            addToCartUseCase(product)
            onSuccess() // Sepete eklendikten sonra UI'a yönlendirme yapması için haber ver
        }
    }
}