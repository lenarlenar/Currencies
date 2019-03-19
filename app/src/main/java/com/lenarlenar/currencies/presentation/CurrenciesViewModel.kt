package com.lenarlenar.currencies.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.lenarlenar.currencies.BuildConfig
import com.lenarlenar.currencies.helpers.CurrencyUtil
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
                                              , private val refreshCommander: RefreshCommander<Long> ) : ViewModel(){

    val currentBaseCurrency = BehaviorSubject.create<Currency>()

    private var currencyRatesUiModelDisposer: Disposable? = null

    private var defaultBaseCurrency = Currency(BuildConfig.DEFAULT_BASE_CURRENCY_CODE
                                                , BuildConfig.DEFAULT_BASE_CURRENCY_AMOUNT
                                                , CurrencyUtil.getNameByCode(BuildConfig.DEFAULT_BASE_CURRENCY_CODE)
                                                , CurrencyUtil.getFlagPathByCode(BuildConfig.DEFAULT_BASE_CURRENCY_CODE))



    private val _currencyRatesUiModel = MutableLiveData<CurrencyRatesUiModel>()
    val currencyRatesUiModel: LiveData<CurrencyRatesUiModel> = _currencyRatesUiModel

    init{
        currentBaseCurrency.onNext(defaultBaseCurrency)
    }

    fun onStart() {

        currencyRatesUiModelDisposer = getCurrencyRatesUiModel()
                                    .observeOn(schedulerProvider.ui())
                                    .subscribe (
                                        {

                                            _currencyRatesUiModel.value = it
                                        }

                                    )


    }

    private fun getRatesWithBase() = currenciesRepository.getRates(currentBaseCurrency.value!!.code)
                                        .map { currencies ->

                                            val result = mutableListOf<Currency>()
                                            currencies.rates.forEach {

                                                val rate = it.rate * currentBaseCurrency.value!!.rate
                                                val name = CurrencyUtil.getNameByCode(it.code)
                                                val flagUrl = CurrencyUtil.getFlagPathByCode(it.code)

                                                result.add(Currency(it.code, rate, name, flagUrl))
                                            }

                                            listOf(currentBaseCurrency.value!!, *result.toTypedArray())
                                        }


    private fun createCurrencyRatesUiModel(baseCurrencyChanged: Boolean)
                        = getRatesWithBase().map {
                                                CurrencyRatesUiModel(baseCurrencyChanged, it)
                                            }


    private fun getCurrencyRatesUiModel()
                        = Observable.merge(refreshCommander.`do`().map{ false }, currentBaseCurrency.map{ true })
                        .flatMap {
                                baseCurrencyChanged ->
                                    createCurrencyRatesUiModel(baseCurrencyChanged)
                        }

    fun onStop(){
        currencyRatesUiModelDisposer?.dispose()
    }

    data class CurrencyRatesUiModel(val baseCurrencyChanged: Boolean, val currencies: List<Currency>)
}