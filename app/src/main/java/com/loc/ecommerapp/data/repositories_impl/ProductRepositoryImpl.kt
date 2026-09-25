package com.loc.ecommerapp.data.repositories_impl

import com.google.firebase.firestore.FirebaseFirestore
import com.loc.ecommerapp.domain.entities.Product
import com.loc.ecommerapp.domain.repositories.ProductRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore // Hilt tarafından otomatik sağlanır
) : ProductRepository {

    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            // Firestore'daki "products" koleksiyonuna bağlanıp verileri çeker
            val snapshot = firestore.collection("products").get().await()

            val productList = snapshot.documents.mapNotNull { document ->
                // DocumentSnapshot'tan manuel eşleme veya DTO kullanımı
                Product(
                    id = document.id,
                    name = document.getString("name") ?: "",
                    price = document.getDouble("price") ?: 0.0,
                    availableStock = document.getLong("actual_stock")?.toInt() ?: 0,
                    description = document.getString("description") ?: ""
                )
            }

            Result.success(productList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductById(id: String): Result<Product> {
        return try {
            val document = firestore.collection("products").document(id).get().await()

            if (document.exists()) {
                val product = Product(
                    id = document.id,
                    name = document.getString("name") ?: "",
                    price = document.getDouble("price") ?: 0.0,
                    availableStock = document.getLong("actual_stock")?.toInt() ?: 0,
                    description = document.getString("description") ?: ""
                )
                Result.success(product)
            } else {
                Result.failure(Exception("Ürün bulunamadı"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}