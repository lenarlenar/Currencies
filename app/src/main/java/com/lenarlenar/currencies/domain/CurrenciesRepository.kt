package com.lenarlenar.currencies.domain

import com.lenarlenar.currencies.domain.models.RatesResponse
import io.reactivex.Single

interface CurrenciesRepository {
    fun getRates(base: String): Single<RatesResponse>
}