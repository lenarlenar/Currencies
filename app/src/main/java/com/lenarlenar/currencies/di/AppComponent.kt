package com.lenarlenar.currencies.di
import com.lenarlenar.currencies.presentation.CurrenciesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent{
    fun inject(currenciesFragment: CurrenciesFragment)
}