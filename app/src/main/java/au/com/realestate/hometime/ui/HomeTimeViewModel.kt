package au.com.realestate.hometime.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import au.com.realestate.hometime.config.TramsConfig
import au.com.realestate.hometime.models.ApiResponse
import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.models.TramStop
import au.com.realestate.hometime.network.ApiStatus
import au.com.realestate.hometime.repo.HomeTimeRepo
import au.com.realestate.hometime.repo.HomeTimeRepoType
import kotlinx.coroutines.*

class HomeTimeViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeTimeViewModel(
            HomeTimeRepo.instance,
            TramsConfig.TRAM_NUMBER,
            TramsConfig.TRAM_STOPS
        ) as T
    }
}

class HomeTimeViewModel(
    private val repo: HomeTimeRepoType,
    private val tramNumber: Int,
    private val tramStops: List<TramStop>
) :
    ViewModel() {
    private val _tramData = MutableLiveData<List<HomeTimeDataItem>>()
    val trams: LiveData<List<HomeTimeDataItem>>
        get() = _tramData

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getTrams()
    }

    fun clear() {
        _apiStatus.value = ApiStatus(ApiStatus.State.CLEARED, null)
        val tramStopAndClearedDataItems = tramStops.map { constructTramStopAndClearedItem(it) }
        _tramData.value = tramStopAndClearedDataItems.flatten()
    }

    fun refresh() {
        getTrams()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getTrams() {
        coroutineScope.launch {
            try {
                _apiStatus.value = ApiStatus(ApiStatus.State.LOADING, null)
                val tramsDataResponse =
                    tramStops.map { async { repo.getTrams(it.id, tramNumber) } }.awaitAll()
                val tramStopsAndTrams = tramStops.zip(tramsDataResponse)
                val tramData = tramStopsAndTrams.map { (tramStop, trams) ->
                    constructTramStopAndTramItems(
                        tramStop,
                        trams
                    )
                }
                _tramData.value = tramData.flatten()
                _apiStatus.value = ApiStatus(ApiStatus.State.DONE, null)

            } catch (t: Throwable) {
                _apiStatus.value = ApiStatus(ApiStatus.State.ERROR, t.message)
            }
        }
    }

    private fun constructTramStopAndTramItems(
        tramStop: TramStop,
        tramResponse: ApiResponse<Tram>
    ): List<HomeTimeDataItem> {
        val tramHeader = HomeTimeDataItem.TramStopHeaderItem(tramStop)
        val tramItems = tramResponse.responseObject?.let { tramsData ->
            tramsData.map {
                HomeTimeDataItem.TramDataItem(it)
            }
        }

        return when (tramItems?.size) {
            null, 0 -> listOf(tramHeader).plus(HomeTimeDataItem.TramsNotFoundItem)
            else -> listOf(tramHeader).plus(tramItems)
        }
    }

    private fun constructTramStopAndClearedItem(tramStop: TramStop): List<HomeTimeDataItem> {
        return listOf(
            HomeTimeDataItem.TramStopHeaderItem(tramStop),
            HomeTimeDataItem.ClearedDataItem
        )
    }
}