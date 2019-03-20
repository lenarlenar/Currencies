package com.lenarlenar.currencies.domain.models

import com.lenarlenar.currencies.BuildConfig
import javax.inject.Inject

interface CurrencySettings {
    val defaultBaseCurrency: Currency
}

class CurrencySettingsImpl @Inject constructor() : CurrencySettings {
    override val defaultBaseCurrency = Currency(
        BuildConfig.DEFAULT_BASE_CURRENCY_CODE
        , BuildConfig.DEFAULT_BASE_CURRENCY_AMOUNT
        , CurrencyHelper.getNameByCode(BuildConfig.DEFAULT_BASE_CURRENCY_CODE)
        , CurrencyHelper.getFlagPathByCode(BuildConfig.DEFAULT_BASE_CURRENCY_CODE)
    )
}

