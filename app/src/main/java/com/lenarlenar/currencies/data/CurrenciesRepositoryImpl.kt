package com.lenarlenar.currencies.data

import com.lenarlenar.currencies.domain.CurrenciesRepository
import com.lenarlenar.currencies.domain.models.RatesResponse
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrenciesRepositoryImpl @Inject constructor(private val currenciesApiService: CurrenciesApiService): CurrenciesRepository {

    override fun getRates(base: String): Observable<RatesResponse> {
        return currenciesApiService
                .getRates(base)
                .subscribeOn(Schedulers.io())
    }
}