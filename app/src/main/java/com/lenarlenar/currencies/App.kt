package com.lenarlenar.currencies

import android.app.Application
import com.lenarlenar.currencies.di.AppComponent
import com.lenarlenar.currencies.di.DaggerAppComponent

class App : Application(){
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().build()
    }

}