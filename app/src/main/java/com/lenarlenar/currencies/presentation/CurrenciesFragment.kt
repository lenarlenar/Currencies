package com.lenarlenar.currencies.presentation

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lenarlenar.currencies.App

import com.lenarlenar.currencies.R
import javax.inject.Inject


class CurrenciesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: CurrenciesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currencies, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        App.appComponent.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrenciesViewModel::class.java)

    }


    companion object {

        @JvmStatic
        fun newInstance() = CurrenciesFragment()
    }
}