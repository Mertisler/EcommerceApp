package com.loc.ecommerapp.data.api.models

import com.google.gson.annotations.SerializedName // Veya kotlinx.serialization

// Ödeme API'sine gönderilecek veri (Request)
data class PaymentRequestDto(
    @SerializedName("total_amount") val totalAmount: Double,
    @SerializedName("currency") val currency: String = "TRY",
    @SerializedName("timestamp") val timestamp: Long
)

// Ödeme API'sinden gelecek yanıt (Response)
data class PaymentResponseDto(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("transaction_id") val transactionId: String?,
    @SerializedName("error_message") val errorMessage: String?
)
