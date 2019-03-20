package com.lenarlenar.currencies.helpers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.lenarlenar.currencies.domain.models.RatesResponse
import java.lang.reflect.Type

class RatesResponseDeserializer : JsonDeserializer<RatesResponse> {
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