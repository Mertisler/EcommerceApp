package com.loc.ecommerapp.domain.use_cases

import com.loc.ecommerapp.domain.entities.Product
import com.loc.ecommerapp.domain.repositories.ProductRepository
import javax.inject.Inject

// İş Kuralı (UseCase)
class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): Result<List<Product>> {
        return productRepository.getProducts()
    }
}