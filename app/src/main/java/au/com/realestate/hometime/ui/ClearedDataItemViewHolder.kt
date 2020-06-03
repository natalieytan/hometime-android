package au.com.realestate.hometime.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import au.com.realestate.hometime.R

class ClearedDataItemViewHolder private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    fun bind(item: HomeTimeDataItem.ClearedDataItem) {
        val headerText: TextView = itemView.findViewById(R.id.textViewNoData)
        headerText.text = "You cleared the data, remember?"
    }

    companion object {
        fun from(parent: ViewGroup): ClearedDataItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.tram_no_data_item, parent, false)
            return ClearedDataItemViewHolder(view)
        }
    }
}