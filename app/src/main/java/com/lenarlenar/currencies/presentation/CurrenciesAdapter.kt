package com.lenarlenar.currencies.presentation

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lenarlenar.currencies.R
import com.lenarlenar.currencies.domain.models.Currency
import kotlinx.android.synthetic.main.fragment_currencies.*
import kotlinx.android.synthetic.main.recycleritem_currency.view.*

class CurrenciesAdapter : RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>(){

    private val items = mutableListOf<Currency>()

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.code.text = items[position].code

        holder.view.rate.setText(String.format("%.2f", items[position].rate))
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context)
            .inflate(R.layout.recycleritem_currency, p0, false) as View
        return ViewHolder(view)
    }

    fun updateData(list: List<Currency>) {

        val callback = DiffUtilCallbackImpl(items, list)
        val diffResult = DiffUtil.calculateDiff(callback)
        items.clear()
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class DiffUtilCallbackImpl(private val oldList: List<Currency>, private val newList: List<Currency>) : DiffUtil.Callback(){

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int)
                = oldList[oldItemPosition].code == newList[newItemPosition].code

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int)
                = oldList[oldItemPosition].rate == newList[newItemPosition].rate

    }
}