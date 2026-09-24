package com.loc.ecommerapp.core.di

import com.loc.ecommerapp.core.network.MockPaymentInterceptor
import com.loc.ecommerapp.data.api.PaymentApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // Interceptor'ı OkHttp istemcisine ekliyoruz
        return OkHttpClient.Builder()
            .addInterceptor(MockPaymentInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.mockpayment.com/")
            .client(okHttpClient) // Özel istemciyi buraya bağladık
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePaymentApi(retrofit: Retrofit): PaymentApi {
        return retrofit.create(PaymentApi::class.java)
    }
}