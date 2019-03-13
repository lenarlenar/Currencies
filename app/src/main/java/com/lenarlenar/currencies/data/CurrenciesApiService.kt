package com.lenarlenar.currencies.data

import com.lenarlenar.currencies.domain.models.RatesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrenciesApiService {

    @GET("latest")
    fun getRates(@Query("base") base: String): Observable<RatesResponse>
}