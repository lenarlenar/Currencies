package com.lenarlenar.currencies.presentation

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lenarlenar.currencies.App

import com.lenarlenar.currencies.R
import kotlinx.android.synthetic.main.fragment_currencies.*
import javax.inject.Inject


class CurrenciesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: CurrenciesViewModel
    private lateinit var currenciesAdapter: CurrenciesAdapter

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

        currenciesAdapter = CurrenciesAdapter(viewModel.currentBaseCurrency)
        val linaerLayoutManager = LinearLayoutManager(context);
        recyclerView.layoutManager = linaerLayoutManager
        recyclerView.adapter = currenciesAdapter
        recyclerView.setHasFixedSize(true)

        viewModel.currenciesStateModel.observe(this, Observer{

            currenciesAdapter.updateData(it!!.currencies)

            if(it.baseCurrencyChanged){
                linaerLayoutManager.scrollToPositionWithOffset(0,0)

            }
        })
    }

    override fun onStart() {
        super.onStart()

        viewModel.onStart()
    }

    override fun onStop() {
        super.onStop()

        viewModel.onStop()
    }

    companion object {

        @JvmStatic
        fun newInstance() = CurrenciesFragment()
    }
}
