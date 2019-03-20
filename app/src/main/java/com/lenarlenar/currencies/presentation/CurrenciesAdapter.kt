package com.lenarlenar.currencies.presentation

import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.lenarlenar.currencies.R
import com.lenarlenar.currencies.domain.models.Currency
import com.lenarlenar.currencies.helpers.ImageLoader
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.recycleritem_currency.view.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat


class CurrenciesAdapter(
    private val currentBaseCurrency: BehaviorSubject<Currency>,
    private val imageLoader: ImageLoader
) : RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>() {

    private val items = mutableListOf<Currency>()
    private val baseItemPosition = 0

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = items[position]

        if (holder.view.code.text == currency.code)
            return

        holder.view.code.text = currency.code
        holder.view.name.text = currency.name
        holder.view.rate.setText(toFormatString(currency.rate))
        imageLoader.load(holder.view.flagIcon, currency.flagUrl!!)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {

        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle = payloads[0] as Bundle
            val newRate = bundle.getDouble("rate")
            holder.view.rate.setText(toFormatString(newRate))
        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val view = LayoutInflater.from(p0.context)
            .inflate(R.layout.recycleritem_currency, p0, false) as View

        val holder = ViewHolder(view)
        holder.view.setOnTouchListener { v, _ ->
            v.rate.requestFocus();
            currentBaseCurrency.onNext(items[holder.adapterPosition])
            true
        }

        holder.view.rate.setOnTouchListener { _, _ ->

            if (holder.adapterPosition != baseItemPosition)
                currentBaseCurrency.onNext(items[holder.adapterPosition])
            false
        }


        holder.view.rate.onTextChanged {
            val position = holder.adapterPosition

            if (position == baseItemPosition) {
                val text = it.trim()
                var currency = items[position]
                currency = Currency(currency.code, if (text.isNotEmpty()) toDouble(text) else 0.0)
                currentBaseCurrency.onNext(currency)
            }
        }

        return holder
    }

    fun swap(list: List<Currency>) {

        val callback = DiffUtilCallbackImpl(items, list)
        val diffResult = DiffUtil.calculateDiff(callback)
        items.clear()
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private class DiffUtilCallbackImpl(private val oldList: List<Currency>, private val newList: List<Currency>) :
        DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].code == newList[newItemPosition].code

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].rate.equals(newList[newItemPosition].rate)

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val diff = Bundle()
            diff.putDouble("rate", newList[newItemPosition].rate)
            return diff
        }

    }

    private fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChanged.invoke(p0?.toString() ?: "")
            }

            override fun afterTextChanged(editable: Editable?) {

            }
        })
    }

    companion object {
        fun toFormatString(number: Double): String {

            if (number.equals(0.0))
                return " "

            val symbols = DecimalFormatSymbols()
            symbols.setGroupingSeparator(' ')
            val format = DecimalFormat("#,##0.00", symbols)

            return format.format(number)
        }

        fun toDouble(str: String): Double {
            val format = NumberFormat.getInstance()
            val number = format.parse(str)
            return number.toDouble()
        }
    }
}