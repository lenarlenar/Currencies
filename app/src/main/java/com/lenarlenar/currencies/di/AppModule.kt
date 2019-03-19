package com.lenarlenar.currencies.di

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.lenarlenar.currencies.data.CurrenciesRepositoryImpl
import com.lenarlenar.currencies.domain.CurrenciesRepository
import com.lenarlenar.currencies.helpers.*
import com.lenarlenar.currencies.presentation.CurrenciesViewModel
import com.lenarlenar.currencies.presentation.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import dagger.multibindings.IntoMap


@Module
interface AppModule{

    @Binds
    @Singleton
    fun bindRepository(repo: CurrenciesRepositoryImpl): CurrenciesRepository

    @Binds
    @Singleton
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CurrenciesViewModel::class)
    @Singleton
    fun bindCurrenciesViewModel(currenciesViewModel: CurrenciesViewModel): ViewModel

    @Binds
    @Singleton
    fun bindImageLoader(imageLoader: ImageLoaderImp) : ImageLoader

    @Binds
    @Singleton
    fun bindSchedulerProvider(schedulerProvider: SchedulerProviderImpl) : SchedulerProvider

    @Binds
    @Singleton
    fun bindRefreshCommander(refreshCommander: RefreshCommanderImpl) : RefreshCommander<Long>

    @Binds
    @Singleton
    fun bindCurrencySettings(currencySettings: CurrencySettingsImpl) : CurrencySettings

}