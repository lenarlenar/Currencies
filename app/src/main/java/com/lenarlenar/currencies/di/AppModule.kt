package com.lenarlenar.currencies.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.lenarlenar.currencies.data.CurrenciesRepositoryImpl
import com.lenarlenar.currencies.domain.CurrenciesRepository
import com.lenarlenar.currencies.presentation.CurrenciesViewModel
import com.lenarlenar.currencies.presentation.ViewModelFactory
import dagger.Binds
import dagger.Module
import javax.inject.Singleton
import dagger.multibindings.IntoMap


@Module
interface AppModule{

    @Binds
    @Singleton
    fun bindRepository(repo: CurrenciesRepositoryImpl): CurrenciesRepository

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CurrenciesViewModel::class)
    fun bindCurrenciesViewModel(currenciesViewModel: CurrenciesViewModel): ViewModel
}