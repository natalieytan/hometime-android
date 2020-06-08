package au.com.realestate.hometime.repo

import au.com.realestate.hometime.datastore.TokenDataStore
import au.com.realestate.hometime.models.ApiResponse
import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.models.TramStop
import au.com.realestate.hometime.network.DataResponse
import au.com.realestate.hometime.network.TramTrackerApi
import au.com.realestate.hometime.network.TramTrackerApiService
import kotlinx.coroutines.*
import java.io.IOException

interface HomeTimeRepoType {
    suspend fun getTramsForTramStops(tramStops: List<TramStop>, tramId: Int): DataResponse<out List<ApiResponse<Tram>>>
}

class HomeTimeRepo(
    private val tokenDataStore: TokenDataStore,
    private val apiService: TramTrackerApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : HomeTimeRepoType {
    companion object {
        val instance: HomeTimeRepoType by lazy {
            HomeTimeRepo(
                TokenDataStore.instance,
                TramTrackerApi.retrofitService
            )
        }
    }

    override suspend fun getTramsForTramStops(
        tramStops: List<TramStop>,
        tramNumber: Int
    ): DataResponse<out List<ApiResponse<Tram>>> = withContext(dispatcher) {
        try {
            val data = asyncFetchTramsForTramStops(tramStops, tramNumber).awaitAll()
            return@withContext DataResponse.Success(data)
        } catch (exception: Exception) {
            return@withContext DataResponse.Error(null, exception.message)
        }
    }

    private suspend fun asyncFetchTramsForTramStops(tramStops: List<TramStop>, tramNumber: Int) = coroutineScope {
        tramStops.map { async { getTrams(it.id, tramNumber) } }
    }

    private suspend fun getTrams(tramStop: Int, tramId: Int): ApiResponse<Tram> {
        return apiService.getTrams(
            tramStop,
            tramId,
            getCachedToken()
        )
    }

    private suspend fun getCachedToken(): String {
        return when (val cachedToken = tokenDataStore.getTramTrackerToken()) {
            null -> getAndCacheToken()
            else -> cachedToken
        }
    }

    private suspend fun getAndCacheToken(): String {
        when (val token = apiService.getToken().responseObject.firstOrNull()?.value) {
            null -> throw IOException("Unable to fetch token")
            else -> {
                tokenDataStore.setTramTrackerToken(token)
                return token
            }
        }
    }
}