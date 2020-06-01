package au.com.realestate.hometime.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import au.com.realestate.hometime.models.ApiResponse
import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.network.ApiStatus
import au.com.realestate.hometime.repo.HomeTimeRepo
import kotlinx.coroutines.*

class HomeTimeViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeTimeViewModel() as T
    }
}

class HomeTimeViewModel : ViewModel() {
    data class HomeTimeData(val north: ApiResponse<Tram>?, val south: ApiResponse<Tram>?)

    private val _trams = MutableLiveData<HomeTimeData>()
    val trams: LiveData<HomeTimeData>
        get() = _trams

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
        _trams.value = HomeTimeData(null, null)
    }

    fun refresh() {
        getTrams()
    }

    private fun getTrams() {
        coroutineScope.launch {
            try {
                _apiStatus.value = ApiStatus(ApiStatus.State.LOADING, null)
                val northTramsDeferred = async { HomeTimeRepo.instance.getNorthTrams() }
                val southTramsDeferred = async { HomeTimeRepo.instance.getSouthTrams() }
                val northTramsData = northTramsDeferred.await()
                val southTramsData = southTramsDeferred.await()
                _trams.value = HomeTimeData(northTramsData, southTramsData)
                _apiStatus.value = ApiStatus(ApiStatus.State.DONE, null)
                Log.i("RESPONSE: NORTH TRAMS", northTramsData.toString())
                Log.i("RESPONSE: SOUTH TRAMS", southTramsData.toString())

            } catch (t: Throwable) {
                _apiStatus.value = ApiStatus(ApiStatus.State.ERROR, t.message)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
