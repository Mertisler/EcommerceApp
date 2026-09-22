package com.loc.ecommerapp.data.api

import com.loc.ecommerapp.data.api.models.PaymentRequestDto
import com.loc.ecommerapp.data.api.models.PaymentResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentApi {

    @POST("v1/payments/process")
    suspend fun processPayment(
        @Body request: PaymentRequestDto
    ): Response<PaymentResponseDto>

}