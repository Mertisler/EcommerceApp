package com.loc.ecommerapp.core.network

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockPaymentInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Gerçek internet gecikmesini simüle etmek için 2 saniye beklet
        Thread.sleep(2000)

        // API'den gelmiş gibi davranacak sahte JSON yanıtı
        val responseJson = """
            {
                "status_code": 200,
                "transaction_id": "TRX-987654321",
                "error_message": null
            }
        """.trimIndent()

        // İsteği ağa çıkarmadan 200 OK ile geri çevir
        return Response.Builder()
            .code(200)
            .message("OK")
            .protocol(Protocol.HTTP_1_1)
            .request(chain.request())
            .body(responseJson.toResponseBody("application/json".toMediaTypeOrNull()))
            .build()
    }
}