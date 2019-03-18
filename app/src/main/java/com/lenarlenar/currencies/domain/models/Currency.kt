package com.lenarlenar.currencies.domain.models

data class Currency(val code: String, var rate: Double, var name: String? = null, var flagUrl: String? = null)