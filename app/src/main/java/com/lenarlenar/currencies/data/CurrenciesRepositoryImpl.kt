package com.lenarlenar.currencies.data

import com.lenarlenar.currencies.domain.CurrenciesRepository
import com.lenarlenar.currencies.domain.models.RatesResponse
import io.reactivex.Observable

class CurrenciesRepositoryImpl : CurrenciesRepository {
    override fun getRates(base: String): Observable<RatesResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}