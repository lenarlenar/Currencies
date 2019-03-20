package com.lenarlenar.currencies.data

import com.lenarlenar.currencies.domain.CurrenciesRepository
import com.lenarlenar.currencies.helpers.SchedulerProvider
import javax.inject.Inject

class CurrenciesRepositoryImpl @Inject constructor(
    private val currenciesApiService: CurrenciesApiService
    , private val schedulerProvider: SchedulerProvider
) : CurrenciesRepository {

    override fun getRates(base: String) = currenciesApiService
        .getRates(base)
        .subscribeOn(schedulerProvider.io())
}