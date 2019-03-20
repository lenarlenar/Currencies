package com.lenarlenar.currencies.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lenarlenar.currencies.BuildConfig
import com.lenarlenar.currencies.data.CurrenciesApiService
import com.lenarlenar.currencies.domain.models.RatesResponse
import com.lenarlenar.currencies.helpers.RatesResponseDeserializer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideGson(): Gson =
        GsonBuilder()
            .registerTypeAdapter(RatesResponse::class.java,
                RatesResponseDeserializer()
            )
            .create()


    @JvmStatic
    @Singleton
    @Provides
    fun provideCurrenciesApiService(gson: Gson): CurrenciesApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.CURRENCY_RATES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CurrenciesApiService::class.java)
    }
}