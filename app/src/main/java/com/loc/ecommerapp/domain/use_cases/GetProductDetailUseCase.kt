package com.loc.ecommerapp.domain.use_cases

import com.loc.ecommerapp.domain.entities.Product
import com.loc.ecommerapp.domain.repositories.ProductRepository
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: String): Result<Product> {
        return productRepository.getProductById(id)
    }
}