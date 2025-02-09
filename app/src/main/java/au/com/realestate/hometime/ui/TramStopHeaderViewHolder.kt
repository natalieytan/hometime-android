package au.com.realestate.hometime.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import au.com.realestate.hometime.R

class TramStopHeaderViewHolder private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    fun bind(tramStopHeaderItem: HomeTimeDataItem.TramStopHeaderItem) {
        val headerText: TextView = itemView.findViewById(R.id.textViewTramStopHeader)
        headerText.text = tramStopHeaderItem.stopName
    }

    companion object {
        fun from(parent: ViewGroup): TramStopHeaderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.tram_stop_header_item, parent, false)
            return TramStopHeaderViewHolder(view)
        }
    }
}