package com.loc.ecommerapp.presentation.states

import com.loc.ecommerapp.domain.entities.Product

sealed interface ProductDetailState {
    object Loading : ProductDetailState
    data class Success(val product: Product) : ProductDetailState
    data class Error(val message: String) : ProductDetailState
}