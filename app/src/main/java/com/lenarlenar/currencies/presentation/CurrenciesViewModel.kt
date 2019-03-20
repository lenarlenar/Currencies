package com.lenarlenar.currencies.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.lenarlenar.currencies.helpers.CurrencySettings
import com.lenarlenar.currencies.domain.CurrenciesRepository
import com.lenarlenar.currencies.domain.models.Currency
import com.lenarlenar.currencies.helpers.RefreshCommander
import com.lenarlenar.currencies.helpers.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class CurrenciesViewModel @Inject constructor(private val currenciesRepository: CurrenciesRepository
                                              , private val  schedulerProvider: SchedulerProvider
                                              , private val refreshCommander: RefreshCommander<Long>
                                              , private val currencySettings : CurrencySettings) : ViewModel(){

    private var currencyRatesUiModelDisposer: Disposable? = null

    private val _currencyRatesUiModel = MutableLiveData<CurrencyRatesUiModel>()
    val currencyRatesUiModel: LiveData<CurrencyRatesUiModel> = _currencyRatesUiModel

    val currentBaseCurrency = BehaviorSubject.create<Currency>()

    init{
        currentBaseCurrency.onNext(currencySettings.defaultBaseCurrency)
    }

    fun onStart() {
        currencyRatesUiModelDisposer = getCurrencyRatesUiModel()
                                    .observeOn(schedulerProvider.ui())
                                    .subscribe{
                                            _currencyRatesUiModel.value = it
                                        }
    }

    fun onStop(){
        currencyRatesUiModelDisposer?.dispose()
    }

    private fun getCurrencyRatesUiModel()
                                = Observable.merge(refreshCommander.`do`().map{ false }, currentBaseCurrency.map{ true })
                                    .flatMap {
                                            baseCurrencyChanged ->
                                        createCurrencyRatesUiModel(baseCurrencyChanged)
                                    }

    private fun createCurrencyRatesUiModel(baseCurrencyChanged: Boolean)
                                = getRatesWithBase().map {
                                                        CurrencyRatesUiModel(baseCurrencyChanged, it)
                                                    }

    private fun getRatesWithBase() = currenciesRepository.getRates(currentBaseCurrency.value!!.code)
                                        .map { currencyRates ->

                                            val currencyRatesWithBase = mutableListOf<Currency>(currentBaseCurrency.value!!)

                                            currencyRates.rates.forEach {
                                                                        currencyRatesWithBase.add(
                                                                            Currency(
                                                                                it.key
                                                                                , it.value * currentBaseCurrency.value!!.rate
                                                                                , currencySettings.getNameByCode(it.key)
                                                                                , currencySettings.getFlagPathByCode(it.key)
                                                                            )
                                                                        )
                                            }

                                            currencyRatesWithBase
                                        }

    data class CurrencyRatesUiModel(val baseCurrencyChanged: Boolean, val currencies: List<Currency>)
}