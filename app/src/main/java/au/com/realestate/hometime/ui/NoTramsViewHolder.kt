package au.com.realestate.hometime.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import au.com.realestate.hometime.R

class NoTramsViewHolder private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    fun bind(item: HomeTimeDataItem.NoTramsItem) {
        val noDataReason: TextView = itemView.findViewById(R.id.textViewNoData)
        noDataReason.text = item.reason
    }

    companion object {
        fun from(parent: ViewGroup): NoTramsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.tram_no_data_item, parent, false)
            return NoTramsViewHolder(view)
        }
    }
}