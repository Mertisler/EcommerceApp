package com.loc.ecommerapp.domain.use_cases

import com.loc.ecommerapp.domain.entities.CartItem
import com.loc.ecommerapp.domain.entities.Product
import com.loc.ecommerapp.domain.repositories.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(product: Product) {
        val cartItem = CartItem(
            productId = product.id,
            productName = product.name,
            unitPrice = product.price,
            quantity = 1 // İlk eklendiğinde miktar 1'dir
        )
        cartRepository.addToCart(cartItem)
    }
}