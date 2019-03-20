package com.lenarlenar.currencies.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lenarlenar.currencies.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, CurrenciesFragment.newInstance())
            .commit()
    }
}
