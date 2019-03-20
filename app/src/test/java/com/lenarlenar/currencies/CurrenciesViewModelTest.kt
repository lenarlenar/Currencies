package com.lenarlenar.currencies

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.lenarlenar.currencies.domain.CurrenciesRepository
import com.lenarlenar.currencies.domain.models.Currency
import com.lenarlenar.currencies.domain.models.CurrencySettingsImpl
import com.lenarlenar.currencies.domain.models.RatesResponse
import com.lenarlenar.currencies.domain.models.RefreshCommander
import com.lenarlenar.currencies.helpers.SchedulerProvider
import com.lenarlenar.currencies.presentation.CurrenciesViewModel
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CurrenciesViewModelTest {

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

        val refreshCommander = object : RefreshCommander<Long> {
            override fun `do`(): Observable<Long> = Observable.empty()
        }

        this.currenciesViewModel =
            CurrenciesViewModel(currenciesRepository, schedulerProvider, refreshCommander,
                CurrencySettingsImpl()
            )

        Mockito.`when`(currenciesRepository.getRates(anyString()))
            .thenAnswer { Single.just(ratesResponse) }
    }

    @Test
    fun `base currency not null after viewModel created`() {

        Assert.assertNotNull(this.currenciesViewModel.currentBaseCurrency.value)
    }

    @Test
    fun `currencyRatesUiModel not null after onStart`() {

        currenciesViewModel.onStart()
        Assert.assertNotNull(this.currenciesViewModel.currencyRatesUiModel.value)
    }

    @Test
    fun `base currency is always on the currencyRatesUiModel's list`() {

        currenciesViewModel.onStart()

        val baseRateCode = currenciesViewModel.currentBaseCurrency.value!!.code

        val baseRateFromCurrenciesStateModel =
            currenciesViewModel.currencyRatesUiModel.value!!.currencies.firstOrNull { it.code == baseRateCode }
        Assert.assertNotNull(baseRateFromCurrenciesStateModel)
    }

    @Test
    fun `when base currency rate changed other rates changing too`() {

        val anotherCurrencyCode = "EUR"
        var anotherCurrencies = mapOf(anotherCurrencyCode to 2.4)

        Mockito.`when`(ratesResponse.rates)
            .thenAnswer { anotherCurrencies }

        currenciesViewModel.onStart()

        val newBaseCurrency = Currency("RUR", 4.0, "ANYNAME", "ANYFLAGPATH")
        currenciesViewModel.currentBaseCurrency.onNext(newBaseCurrency)

        val baseRateFromCurrenciesStateModel =
            this.currenciesViewModel.currencyRatesUiModel.value!!.currencies.first { it.code == anotherCurrencyCode }
        Assert.assertTrue(
            (newBaseCurrency.rate * anotherCurrencies.getValue(anotherCurrencyCode)).equals(
                baseRateFromCurrenciesStateModel.rate
            )
        )
    }

}