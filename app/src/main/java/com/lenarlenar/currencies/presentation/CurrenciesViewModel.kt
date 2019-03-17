package com.lenarlenar.currencies.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.lenarlenar.currencies.domain.CurrenciesRepository
import com.lenarlenar.currencies.domain.models.Currency
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrenciesViewModel @Inject constructor(private val currenciesRepository: CurrenciesRepository) : ViewModel(){

    private var defaultBaseCurrency = Currency("EUR", 100.0)
    private var currenciesListDisposable: Disposable? = null

    private val currentBaseCurrency = BehaviorSubject.create<Currency>()

    private val _currencies = MutableLiveData<List<Currency>>()
    val currencies: LiveData<List<Currency>> = _currencies


    init{
        currentBaseCurrency.onNext(defaultBaseCurrency)
    }

    fun onStart() {
        currenciesListDisposable = getRates()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                        {
                                             _currencies.value = it.rates
                                        }
                                    )
    }

    fun getRates() = Observable.interval(0, 1, TimeUnit.SECONDS)
                        .flatMap { currenciesRepository.getRates(defaultBaseCurrency.code) }


    fun onStop(){
        currenciesListDisposable?.dispose()
    }

}