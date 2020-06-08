package au.com.realestate.hometime.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import au.com.realestate.hometime.HomeTimeApplication
import au.com.realestate.hometime.R
import au.com.realestate.hometime.config.TramsConfig
import au.com.realestate.hometime.models.ApiResponse
import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.models.TramStop
import au.com.realestate.hometime.network.DataResponse
import au.com.realestate.hometime.repo.HomeTimeRepo
import au.com.realestate.hometime.repo.HomeTimeRepoType
import au.com.realestate.hometime.utils.TimeUtility
import au.com.realestate.hometime.utils.TimeUtils
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HomeTimeViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeTimeViewModel(
            HomeTimeRepo.instance,
            TramsConfig.TRAM_NUMBER,
            TramsConfig.TRAM_STOPS,
            TimeUtils
        ) as T
    }
}

class HomeTimeViewModel(
    private val repo: HomeTimeRepoType,
    private val tramNumber: Int,
    private val tramStops: List<TramStop>,
    private val timeUtil: TimeUtility
) :
    ViewModel(), CoroutineScope {
    private val _dataResponse = MutableLiveData<DataResponse<out List<ApiResponse<Tram>>>>()
    val dataResponse: LiveData<DataResponse<out List<ApiResponse<Tram>>>>
        get() = _dataResponse

    private val _homeTimeItems = MutableLiveData<List<HomeTimeDataItem>>()
    val homeTimeItems: LiveData<List<HomeTimeDataItem>>
        get() = _homeTimeItems

    private var viewModelJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = viewModelJob + Dispatchers.Main

    init {
        getTrams()
    }

    fun clear() {
        _dataResponse.value = DataResponse.Cleared()
        val tramStopAndClearedDataItems = tramStops.map { constructTramStopAndClearedItem(it) }
        _homeTimeItems.value = tramStopAndClearedDataItems.flatten()
    }

    fun refresh() {
        getTrams()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancelChildren()
        viewModelJob.cancel()
    }

    private fun getTrams() {
        launch {
            _dataResponse.value = DataResponse.Loading()
            val timeFetched = timeUtil.formattedCurrentDateTimeString()
            val dataResponse = repo.getTramsForTramStops(tramStops, tramNumber)
            _dataResponse.value = dataResponse
            val homeTimeItems = dataResponse.data?.let {
                constructItems(timeFetched, it)
            }
            _homeTimeItems.value = homeTimeItems ?: emptyList()
        }
    }

    private fun constructItems(
        timeString: String,
        data: List<ApiResponse<Tram>>
    ): List<HomeTimeDataItem> {
        val tramItems = tramStops.zip(data).map { (tramStop, trams) ->
            constructTramStopAndTramItems(
                tramStop,
                trams
            )
        }.flatten()
        val lastUpdatedItem = HomeTimeDataItem.LastUpdatedTime(timeString)
        return listOf(lastUpdatedItem).plus(tramItems)
    }

    private fun constructTramStopAndTramItems(
        tramStop: TramStop,
        tramResponse: ApiResponse<Tram>
    ): List<HomeTimeDataItem> {
        val tramHeader = HomeTimeDataItem.TramStopHeaderItem(tramStop)
        val tramItems = tramResponse.responseObject.let { tramsData ->
            tramsData.map {
                HomeTimeDataItem.TramDataItem(it)
            }
        }

        return when (tramItems.size) {
            0 -> listOf(tramHeader).plus(
                HomeTimeDataItem.NoTramsItem(
                    HomeTimeApplication.context.getString(
                        R.string.trams_not_found
                    )
                )
            )
            else -> listOf(tramHeader).plus(tramItems)
        }
    }

    private fun constructTramStopAndClearedItem(tramStop: TramStop): List<HomeTimeDataItem> {
        return listOf(
            HomeTimeDataItem.TramStopHeaderItem(tramStop),
            HomeTimeDataItem.NoTramsItem(HomeTimeApplication.context.getString(R.string.tram_data_cleared))
        )
    }
}