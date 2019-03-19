package com.lenarlenar.currencies

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.lenarlenar.currencies.domain.CurrenciesRepository
import com.lenarlenar.currencies.domain.models.Currency
import com.lenarlenar.currencies.domain.models.RatesResponse
import com.lenarlenar.currencies.helpers.RefreshCommander
import com.lenarlenar.currencies.helpers.SchedulerProvider
import com.lenarlenar.currencies.presentation.CurrenciesViewModel
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.ArgumentMatchers.*

class CurrenciesViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var currenciesRepository: CurrenciesRepository

    @Mock
    lateinit var ratesResponse: RatesResponse

    lateinit var currenciesViewModel: CurrenciesViewModel

    private val schedulerProvider: SchedulerProvider = TrampolineSchedulerProvider()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val refreshCommander = object: RefreshCommander<Long>{
            override fun `do`(): Observable<Long>  = Observable.empty()
        }

        this.currenciesViewModel = CurrenciesViewModel(currenciesRepository, schedulerProvider, refreshCommander)

        Mockito.`when`(currenciesRepository.getRates(anyString()))
            .thenAnswer {Observable.just(ratesResponse)}
    }

    @Test
    fun `base currency not null after viewModel created`(){

        Assert.assertNotNull(this.currenciesViewModel.currentBaseCurrency.value)
    }

    @Test
    fun `currencyRatesUiModel not null after onStart`(){

        currenciesViewModel.onStart()
        Assert.assertNotNull(this.currenciesViewModel.currencyRatesUiModel.value)
    }

    @Test
    fun `base currency is always on the currencyRatesUiModel's list`(){

        currenciesViewModel.onStart()

        val baseRateCode = currenciesViewModel.currentBaseCurrency.value!!.code

        val baseRateFromCurrenciesStateModel = currenciesViewModel.currencyRatesUiModel.value!!.currencies.firstOrNull{ it.code == baseRateCode}

        Assert.assertNotNull(baseRateFromCurrenciesStateModel)
    }

    @Test
    fun `when base currency rate changed other rates changing too`(){

        var anotherCurrency = Currency("CO1", 2.4)

        Mockito.`when`(ratesResponse.rates)
            .thenAnswer { listOf(anotherCurrency) }

        currenciesViewModel.onStart()

        val newBaseCurrency = Currency("CO2", 4.0)
        currenciesViewModel.currentBaseCurrency.onNext(newBaseCurrency)

        val baseRateFromCurrenciesStateModel = this.currenciesViewModel.currencyRatesUiModel.value!!.currencies.firstOrNull{ it.code == anotherCurrency.code}

        Assert.assertTrue((newBaseCurrency.rate * anotherCurrency.rate).equals(baseRateFromCurrenciesStateModel!!.rate))
    }

}