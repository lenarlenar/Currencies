package com.lenarlenar.currencies.domain.models

import com.google.gson.annotations.SerializedName

data class RatesResponse(@SerializedName("date")
                         val date: String?,
                         @SerializedName("rates")
                         val rates: List<Currency>,
                         @SerializedName("base")
                         val base: String?)