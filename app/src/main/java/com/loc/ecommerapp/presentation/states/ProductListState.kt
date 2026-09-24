package com.loc.ecommerapp.presentation.states

import com.loc.ecommerapp.domain.entities.Product

sealed interface ProductListState {
    object Loading : ProductListState
    data class Success(val products: List<Product>) : ProductListState
    data class Error(val message: String) : ProductListState
}