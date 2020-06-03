package au.com.realestate.hometime.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import au.com.realestate.hometime.R

class TramsNotFoundItemViewHolder private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    fun bind(item: HomeTimeDataItem.TramsNotFoundItem) {
        val headerText: TextView = itemView.findViewById(R.id.textViewNoData)
        headerText.text = "No data found"
    }

    companion object {
        fun from(parent: ViewGroup): TramsNotFoundItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.tram_no_data_item, parent, false)
            return TramsNotFoundItemViewHolder(view)
        }
    }
}