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
        val tramId: TextView = itemView.findViewById(R.id.textViewTramId)
        val tramText: TextView = itemView.findViewById(R.id.textViewTramRoute)
        val arrivalTime: TextView = itemView.findViewById(R.id.textViewArrivalTime)
        val minutesAway: TextView = itemView.findViewById(R.id.textViewMinutesAway)
        val destination: TextView = itemView.findViewById(R.id.textViewDestination)

        tramId.text = tramDataItem.tramId
        tramText.text = tramDataItem.tramRoute
        arrivalTime.text = tramDataItem.arrivalDate
        minutesAway.text = tramDataItem.displayArrivalTime
        destination.text = tramDataItem.destination
    }

    companion object {
        fun from(parent: ViewGroup): TramItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.tram_data_item, parent, false)
            return TramItemViewHolder(view)
        }
    }
}