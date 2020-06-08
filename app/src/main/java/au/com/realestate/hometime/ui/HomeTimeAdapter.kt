package au.com.realestate.hometime.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.models.TramStop
import au.com.realestate.hometime.utils.TimeUtility
import au.com.realestate.hometime.utils.TimeUtils

class HomeTimeAdapter :
    ListAdapter<HomeTimeDataItem, RecyclerView.ViewHolder>(HomeTimeDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HomeTimeDataItemTypes.TRAM_STOP_HEADER -> TramStopHeaderViewHolder.from(parent)
            HomeTimeDataItemTypes.TRAM_DATA -> TramItemViewHolder.from(parent)
            HomeTimeDataItemTypes.NO_TRAMS -> NoTramsViewHolder.from(parent)
            HomeTimeDataItemTypes.LAST_UPDATED_TIME_HEADER -> LastUpdatedTimeViewHolder.from(parent)
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
            is NoTramsViewHolder -> {
                val item = getItem(position) as HomeTimeDataItem.NoTramsItem
                holder.bind(item)
            }
            is LastUpdatedTimeViewHolder -> {
                val item = getItem(position) as HomeTimeDataItem.LastUpdatedTime
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeTimeDataItem.TramStopHeaderItem -> HomeTimeDataItemTypes.TRAM_STOP_HEADER
            is HomeTimeDataItem.TramDataItem -> HomeTimeDataItemTypes.TRAM_DATA
            is HomeTimeDataItem.NoTramsItem -> HomeTimeDataItemTypes.NO_TRAMS
            is HomeTimeDataItem.LastUpdatedTime -> HomeTimeDataItemTypes.LAST_UPDATED_TIME_HEADER
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
    const val NO_TRAMS = 3
    const val LAST_UPDATED_TIME_HEADER = 4
}

sealed class HomeTimeDataItem {
    class TramStopHeaderItem(tramStop: TramStop) : HomeTimeDataItem() {
        override val itemId = HomeTimeDataItemTypes.TRAM_STOP_HEADER
        override val dataId = tramStop.id

        val stopName = tramStop.name
    }

    class TramDataItem(tram: Tram, timeUtil: TimeUtility = TimeUtils) : HomeTimeDataItem() {
        override val itemId = HomeTimeDataItemTypes.TRAM_DATA
        override val dataId = tram.vehicleNo

        private val predictedArrivalInEpochMilliseconds =
            tram.predictedArrival?.let { timeUtil.timeInMillisecondsFromUnixTime(it) }

        val tramId = "#${tram.vehicleNo?.toString()?.padStart(4, '0')}"
        val tramRoute = tram.routeNo
        val destination = tram.destination
        val displayArrivalTime =
            predictedArrivalInEpochMilliseconds?.let {
                timeUtil.formattedTimeDifferenceFromCurrentTime(
                    it
                )
            }
        val arrivalDate =
            predictedArrivalInEpochMilliseconds?.let { timeUtil.formattedDateTimeString(it) }
    }

    data class NoTramsItem(val reason: String) : HomeTimeDataItem() {
        override val itemId = HomeTimeDataItemTypes.NO_TRAMS
    }

    data class LastUpdatedTime(val timeString: String) : HomeTimeDataItem() {
        override val itemId = HomeTimeDataItemTypes.LAST_UPDATED_TIME_HEADER
    }

    abstract val itemId: Int
    open val dataId: Int? = null
}