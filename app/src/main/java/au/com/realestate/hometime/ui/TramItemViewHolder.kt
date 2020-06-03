package au.com.realestate.hometime.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import au.com.realestate.hometime.R

class TramItemViewHolder private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    fun bind(tramDataItem: HomeTimeDataItem.TramDataItem) {
        val tramText: TextView = itemView.findViewById(R.id.textViewTramNumber)
        tramText.text = tramDataItem.tram.vehicleNo.toString()
    }

    companion object {
        fun from(parent: ViewGroup): TramItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.tram_data_item, parent, false)
            return TramItemViewHolder(view)
        }
    }
}