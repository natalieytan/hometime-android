package au.com.realestate.hometime.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import au.com.realestate.hometime.R

class LastUpdatedTimeViewHolder private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    fun bind(item: HomeTimeDataItem.LastUpdatedTime) {
        val updatedTime: TextView = itemView.findViewById(R.id.textViewLastUpdatedTime)
        updatedTime.text = item.timeString
    }

    companion object {
        fun from(parent: ViewGroup): LastUpdatedTimeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.tram_last_updated_time_item, parent, false)
            return LastUpdatedTimeViewHolder(view)
        }
    }
}