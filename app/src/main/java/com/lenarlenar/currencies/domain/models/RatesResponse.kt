package com.lenarlenar.currencies.domain.models

data class RatesResponse(
    val date: String?
    , val rates: Map<String, Double>
    , val base: String?
)