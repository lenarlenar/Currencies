package com.lenarlenar.currencies.domain.models

import com.google.gson.annotations.SerializedName

data class RatesResponse(@SerializedName("date")
                         val date: String?,
                         @SerializedName("rates")
                         val rates: Map<String, Double>,
                         @SerializedName("base")
                         val base: String?)