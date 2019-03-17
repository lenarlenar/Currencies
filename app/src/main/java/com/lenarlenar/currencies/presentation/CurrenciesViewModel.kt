package com.lenarlenar.currencies.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.lenarlenar.currencies.BuildConfig
import com.lenarlenar.currencies.domain.CurrenciesRepository
import com.lenarlenar.currencies.domain.models.Currency
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrenciesViewModel @Inject constructor(private val currenciesRepository: CurrenciesRepository) : ViewModel(){

    val currentBaseCurrency = BehaviorSubject.create<Currency>()

    private val timerObservable = Observable.interval(0, BuildConfig.UPDATE_INTERVAL_SECONDS_AMOUNT, TimeUnit.SECONDS)

    private var currenciesStateModelDisposable: Disposable? = null

    private var defaultBaseCurrency = Currency(BuildConfig.DEFAULT_BASE_CURRENCY_CODE, BuildConfig.DEFAULT_BASE_CURRENCY_AMOUNT)

    private val _currenciesStateModel = MutableLiveData<CurrenciesStateModel>()
    val currenciesStateModel: LiveData<CurrenciesStateModel> = _currenciesStateModel

    init{
        currentBaseCurrency.onNext(defaultBaseCurrency)
    }

    fun onStart() {
        currenciesStateModelDisposable = getCurrenciesStateModel()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe {
                                        _currenciesStateModel.value = it
                                    }

    }

    private fun getRatesWithBase() = currenciesRepository.getRates(currentBaseCurrency.value!!.code)
        .map {
            it.rates.forEach {
                it.rate = it.rate * currentBaseCurrency.value!!.rate
            }
            listOf(currentBaseCurrency.value!!, *it.rates.toTypedArray())
        }

    private fun createCurrenciesStateModel(baseCurrencyChanged: Boolean)
            = getRatesWithBase().map {
                                    CurrenciesStateModel(baseCurrencyChanged, it)
                                }


    private fun getCurrenciesStateModel() = Observable
                        .merge(timerObservable.map{ false }, currentBaseCurrency.map{ true })
                        .flatMap {
                                baseCurrencyChanged -> createCurrenciesStateModel(baseCurrencyChanged)
                        }

    fun onStop(){
        currenciesStateModelDisposable?.dispose()
    }

    data class CurrenciesStateModel(val baseCurrencyChanged: Boolean, val currencies: List<Currency>)
}