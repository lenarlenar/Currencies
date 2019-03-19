package com.lenarlenar.currencies.domain.models

import android.provider.Telephony
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import kotlin.concurrent.fixedRateTimer

class RatesResponseDeserializer : JsonDeserializer<RatesResponse>{
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): RatesResponse {

        val currenciesRatesResponseJsonObject = json?.asJsonObject

        val currencies = mutableMapOf<String, Double>()
        val currenciesRatesJsonObject = currenciesRatesResponseJsonObject?.get("rates")?.asJsonObject

        currenciesRatesJsonObject?.entrySet()?.forEach {
            currencies[it.key] = it.value.asDouble
        }

        val ratesResponse = RatesResponse(
            currenciesRatesResponseJsonObject?.get("date")?.asString
            , currencies
            , currenciesRatesResponseJsonObject?.get("base")?.asString
        )

        return ratesResponse
    }

}