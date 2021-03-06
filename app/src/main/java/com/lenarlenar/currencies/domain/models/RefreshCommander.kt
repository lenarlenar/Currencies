package com.lenarlenar.currencies.domain.models

import com.lenarlenar.currencies.BuildConfig
import com.lenarlenar.currencies.helpers.SchedulerProvider
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface RefreshCommander<T> {
    fun `do`(): Observable<T>
}

class RefreshCommanderImpl @Inject constructor(private val schedulerProvider: SchedulerProvider) :
    RefreshCommander<Long> {

    override fun `do`() = Observable.interval(
        0,
        BuildConfig.UPDATE_INTERVAL_SECONDS_AMOUNT,
        TimeUnit.SECONDS,
        schedulerProvider.computation()
    )
}