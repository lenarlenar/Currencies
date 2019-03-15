package com.lenarlenar.currencies.presentation

import android.arch.lifecycle.ViewModel
import com.lenarlenar.currencies.domain.CurrenciesRepository
import javax.inject.Inject

class CurrenciesViewModel @Inject constructor(private val currenciesRepository: CurrenciesRepository) : ViewModel(){

}