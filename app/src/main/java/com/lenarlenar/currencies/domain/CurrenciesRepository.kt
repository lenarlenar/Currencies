package com.lenarlenar.currencies.domain

import com.lenarlenar.currencies.domain.models.RatesResponse
import io.reactivex.Observable

interface CurrenciesRepository {
    fun getRates(base: String): Observable<RatesResponse>
}