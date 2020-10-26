package com.bulletmys.bitcoin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

class BTCAdapter(
    private val BTCDataSet: ArrayList<BTCListItem>,
    private val onElemClickListener: (BTCListItem) -> Unit
) : RecyclerView.Adapter<BTCAdapter.BTCViewHolder>() {

    class BTCViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val dateRow: TextView = view.findViewById(R.id.btc_date_view)
        private val valueRow: TextView = view.findViewById(R.id.btc_value_view)
        private val currRow: TextView = view.findViewById(R.id.btc_currency_view)


        fun setData(item: BTCListItem) {
            val sdf = SimpleDateFormat("dd/MM")

            dateRow.text = sdf.format(item.date)
            valueRow.text = item.cost.toString()
            currRow.text = item.currency
        }

        fun setListener(clickListener: (BTCListItem) -> Unit, item: BTCListItem) {
            itemView.setOnClickListener { clickListener(item) }
        }
    }

    fun setItems(data: ArrayList<BTCListItem>) {
        BTCDataSet.clear()
        BTCDataSet.addAll(data)
        notifyDataSetChanged()
    }

    fun clearItems() {
        BTCDataSet.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BTCViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false) as RelativeLayout
        return BTCViewHolder(textView)
    }

    override fun onBindViewHolder(holder: BTCViewHolder, position: Int) {
        val item = BTCDataSet[position]
        holder.setListener(onElemClickListener, item)
        holder.setData(item)
    }

    override fun getItemCount() = BTCDataSet.size
}