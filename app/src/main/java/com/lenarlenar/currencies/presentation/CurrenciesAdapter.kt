package com.lenarlenar.currencies.presentation

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
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.recycleritem_currency.view.*
import org.w3c.dom.Text
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols




class CurrenciesAdapter (private val currentBaseCurrency: BehaviorSubject<Currency>) : RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>(){

    private val items = mutableListOf<Currency>()

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.view.code.text = items[position].code
        holder.view.rate.setText(decimalFormat(items[position].rate))

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val view = LayoutInflater.from(p0.context)
            .inflate(R.layout.recycleritem_currency, p0, false) as View

        val holder = ViewHolder(view)
        holder.view.setOnClickListener {
            currentBaseCurrency.onNext(items[holder.adapterPosition])
        }

        holder.view.rate.setOnTouchListener { _, _ ->
            if(holder.adapterPosition != 0)
                currentBaseCurrency.onNext(items[holder.adapterPosition])
             false
        }


        holder.view.rate.onTextChanged {
            val position = holder.adapterPosition

            if(position == 0){
                val currency = items[position]
                currency.rate = if(it.isNotEmpty()) it.toDouble() else 0.0
                currentBaseCurrency.onNext(currency)
            }
        }

        return holder
    }

    fun updateData(list: List<Currency>) {

        val callback = DiffUtilCallbackImpl(items, list)
        val diffResult = DiffUtil.calculateDiff(callback)
        items.clear()
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private class DiffUtilCallbackImpl(private val oldList: List<Currency>, private val newList: List<Currency>) : DiffUtil.Callback(){

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int)
                = oldList[oldItemPosition].code == newList[newItemPosition].code

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int)
                = oldList[oldItemPosition].rate == newList[newItemPosition].rate

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
        fun decimalFormat(number: Double): String {
            val symbols = DecimalFormatSymbols()
            symbols.setGroupingSeparator(' ')
            val format = DecimalFormat("#,###.00", symbols)

            return format.format(number)
        }
    }
}