package com.loc.ecommerapp.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.ecommerapp.domain.entities.Product
import com.loc.ecommerapp.domain.use_cases.AddToCartUseCase
import com.loc.ecommerapp.domain.use_cases.GetProductsUseCase
import com.loc.ecommerapp.presentation.states.ProductListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    // Ekran açılır açılmaz ilk durum "Loading" (Yükleniyor) olarak başlar
    private val _uiState = MutableStateFlow<ProductListState>(ProductListState.Loading)
    val uiState: StateFlow<ProductListState> = _uiState.asStateFlow()

    init {
        // ViewModel yaratıldığı an ürünleri çekmeye başla
        fetchProducts()
    }

    fun fetchProducts() {
        _uiState.value = ProductListState.Loading

        viewModelScope.launch {
            val result = getProductsUseCase()

            result.fold(
                onSuccess = { products ->
                    _uiState.value = ProductListState.Success(products)
                },
                onFailure = { exception ->
                    val errorMsg = exception.message ?: "Ürünler yüklenemedi."
                    _uiState.value = ProductListState.Error(errorMsg)
                }
            )
        }
    }
    fun addToCart(product: Product) {
        viewModelScope.launch {
            addToCartUseCase(product)
        }
    }
}