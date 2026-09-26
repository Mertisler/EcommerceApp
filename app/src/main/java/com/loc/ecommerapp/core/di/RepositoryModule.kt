package com.loc.ecommerapp.core.di

import com.loc.ecommerapp.data.repositories_impl.AuthRepositoryImpl
import com.loc.ecommerapp.data.repositories_impl.CartRepositoryImpl
import com.loc.ecommerapp.data.repositories_impl.OrderRepositoryImpl
import com.loc.ecommerapp.data.repositories_impl.ProductRepositoryImpl
import com.loc.ecommerapp.domain.repositories.AuthRepository
import com.loc.ecommerapp.domain.repositories.CartRepository
import com.loc.ecommerapp.domain.repositories.OrderRepository
import com.loc.ecommerapp.domain.repositories.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository


    @Binds
    @Singleton
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}

