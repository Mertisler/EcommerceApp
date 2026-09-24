package com.loc.ecommerapp.domain.repositories

import com.loc.ecommerapp.domain.entities.Product

// Sözleşme (Interface)
interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>

    suspend fun getProductById(id: String): Result<Product>
}