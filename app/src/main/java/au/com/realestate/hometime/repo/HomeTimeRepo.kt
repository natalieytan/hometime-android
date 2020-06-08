package au.com.realestate.hometime.repo

import au.com.realestate.hometime.datastore.TokenDataStore
import au.com.realestate.hometime.models.ApiResponse
import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.network.TramTrackerApi
import au.com.realestate.hometime.network.TramTrackerApiService
import java.io.IOException

interface HomeTimeRepoType {
    suspend fun getTrams(tramStop: Int, tramId: Int): ApiResponse<Tram>
}

class HomeTimeRepo(private val tokenDataStore: TokenDataStore, private val apiService: TramTrackerApiService) : HomeTimeRepoType {
    companion object {
        val instance: HomeTimeRepoType by lazy {
            HomeTimeRepo(
                TokenDataStore.instance,
                TramTrackerApi.retrofitService
            )
        }
    }

    override suspend fun getTrams(tramStop: Int, tramId: Int): ApiResponse<Tram> {
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
        when(val token = apiService.getToken().responseObject?.firstOrNull()?.value) {
            null -> throw IOException("Unable to fetch token")
            else -> {
                tokenDataStore.setTramTrackerToken(token)
                return token
            }
        }
    }
}