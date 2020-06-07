package au.com.realestate.hometime.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.models.TramStop
import au.com.realestate.hometime.utils.TimeUtils.dateString
import au.com.realestate.hometime.utils.TimeUtils.formattedDisplay
import au.com.realestate.hometime.utils.TimeUtils.timeDifferenceFromNowInMinutes
import au.com.realestate.hometime.utils.TimeUtils.timeFromUnixTime

class HomeTimeAdapter :
    ListAdapter<HomeTimeDataItem, RecyclerView.ViewHolder>(HomeTimeDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HomeTimeDataItemTypes.TRAM_STOP_HEADER -> TramStopHeaderViewHolder.from(parent)
            HomeTimeDataItemTypes.TRAM_DATA -> TramItemViewHolder.from(parent)
            HomeTimeDataItemTypes.TRAMS_NOT_FOUND -> TramsNotFoundItemViewHolder.from(parent)
            HomeTimeDataItemTypes.CLEARED_DATA -> ClearedDataItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TramStopHeaderViewHolder -> {
                val item = getItem(position) as HomeTimeDataItem.TramStopHeaderItem
                holder.bind(item)
            }
            is TramItemViewHolder -> {
                val item = getItem(position) as HomeTimeDataItem.TramDataItem
                holder.bind(item)
            }
            is TramsNotFoundItemViewHolder -> {
                val item = getItem(position) as HomeTimeDataItem.TramsNotFoundItem
                holder.bind(item)
            }
            is ClearedDataItemViewHolder -> {
                val item = getItem(position) as HomeTimeDataItem.ClearedDataItem
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeTimeDataItem.TramStopHeaderItem -> HomeTimeDataItemTypes.TRAM_STOP_HEADER
            is HomeTimeDataItem.TramDataItem -> HomeTimeDataItemTypes.TRAM_DATA
            HomeTimeDataItem.TramsNotFoundItem -> HomeTimeDataItemTypes.TRAMS_NOT_FOUND
            HomeTimeDataItem.ClearedDataItem -> HomeTimeDataItemTypes.CLEARED_DATA
        }
    }
}

class HomeTimeDiffCallBack : DiffUtil.ItemCallback<HomeTimeDataItem>() {
    override fun areItemsTheSame(oldItem: HomeTimeDataItem, newItem: HomeTimeDataItem): Boolean {
        return oldItem.itemId == newItem.itemId && oldItem.dataId == newItem.dataId
    }

    override fun areContentsTheSame(oldItem: HomeTimeDataItem, newItem: HomeTimeDataItem): Boolean {
        return oldItem == newItem
    }
}

object HomeTimeDataItemTypes {
    const val TRAM_STOP_HEADER = 1
    const val TRAM_DATA = 2
    const val TRAMS_NOT_FOUND = 3
    const val CLEARED_DATA = 4
}

sealed class HomeTimeDataItem {
    class TramStopHeaderItem(tramStop: TramStop) : HomeTimeDataItem() {
        override val itemId = HomeTimeDataItemTypes.TRAM_STOP_HEADER
        override val dataId = tramStop.id

        val stopName = tramStop.name
    }

    class TramDataItem(tram: Tram) : HomeTimeDataItem() {
        override val itemId = HomeTimeDataItemTypes.TRAM_DATA
        override val dataId = tram.vehicleNo

        private val currentTimeEpochSeconds = tram.predictedArrival?.let { timeFromUnixTime(it) }
        private val minutesAway = currentTimeEpochSeconds?.let { timeDifferenceFromNowInMinutes(it) }


        val tramId = "#${tram.vehicleNo?.toString()?.padStart(3, '0')}"
        val tramRoute = tram.routeNo
        val destination = tram.destination
        val displayArrivalTime = minutesAway?.let { formattedDisplay(it) }
        val arrivalDate = currentTimeEpochSeconds?.let { dateString(it) }
    }

    object TramsNotFoundItem : HomeTimeDataItem() {
        override val itemId = HomeTimeDataItemTypes.TRAMS_NOT_FOUND
    }

    object ClearedDataItem : HomeTimeDataItem() {
        override val itemId = HomeTimeDataItemTypes.CLEARED_DATA
    }

    abstract val itemId: Int
    open val dataId: Int? = null
}