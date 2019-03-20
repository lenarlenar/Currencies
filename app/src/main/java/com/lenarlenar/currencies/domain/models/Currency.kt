package com.lenarlenar.currencies.domain.models

data class Currency(
    val code: String
    , val rate: Double
    , val name: String? = null
    , val flagUrl: String? = null
)