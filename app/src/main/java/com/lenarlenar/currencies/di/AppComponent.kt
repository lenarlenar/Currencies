package com.lenarlenar.currencies.di

import com.lenarlenar.currencies.presentation.CurrenciesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(currenciesFragment: CurrenciesFragment)
}