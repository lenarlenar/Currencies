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
abstract class AppModule{

    @Binds
    @Singleton
    abstract fun bindRepository(repo: CurrenciesRepositoryImpl): CurrenciesRepository

    @Binds
    @Singleton
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CurrenciesViewModel::class)
    @Singleton
    abstract fun bindCurrenciesViewModel(currenciesViewModel: CurrenciesViewModel): ViewModel
}