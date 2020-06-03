package au.com.realestate.hometime.repo

import au.com.realestate.hometime.datastore.TokenDataStore
import au.com.realestate.hometime.models.ApiResponse
import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.network.TramTrackerApi

interface HomeTimeRepoType {
    suspend fun getTrams(tramStop: Int, tramId: Int): ApiResponse<Tram>
}

class HomeTimeRepo(private val tokenDataStore: TokenDataStore) : HomeTimeRepoType {
    companion object {
        val instance: HomeTimeRepoType by lazy {
            HomeTimeRepo(TokenDataStore.instance)
        }
    }

    override suspend fun getTrams(tramStop: Int, tramId: Int): ApiResponse<Tram> {
        return TramTrackerApi.retrofitService.getTrams(
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
        val token = TramTrackerApi.retrofitService.getToken().responseObject?.firstOrNull()?.value
            ?: "default_token"
        tokenDataStore.setTramTrackerToken(token)
        return token
    }
}