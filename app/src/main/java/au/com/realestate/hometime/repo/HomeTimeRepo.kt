package au.com.realestate.hometime.repo

import au.com.realestate.hometime.config.HomeTimeTramsConfig
import au.com.realestate.hometime.models.ApiResponse
import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.network.TramTrackerApi

interface HomeTimeRepoType {
    suspend fun getToken(): String
    suspend fun getNorthTrams(): ApiResponse<Tram>
    suspend fun getSouthTrams(): ApiResponse<Tram>
}

class HomeTimeRepo : HomeTimeRepoType {
    companion object {
        val instance: HomeTimeRepoType by lazy {
            HomeTimeRepo()
        }
    }

    override suspend fun getToken(): String {
        return "97d78b56-8fc7-4dc3-a0d3-2cad0cb2e980"
    }

    override suspend fun getNorthTrams(): ApiResponse<Tram> {
        return TramTrackerApi.retrofitService.getTrams(
            HomeTimeTramsConfig.NORTH_TRAM_STOP_ID,
            HomeTimeTramsConfig.TRAM_NUMBER,
            getToken()
        )
    }

    override suspend fun getSouthTrams(): ApiResponse<Tram> {
        return TramTrackerApi.retrofitService.getTrams(
            HomeTimeTramsConfig.SOUTH_TRAM_STOP_ID,
            HomeTimeTramsConfig.TRAM_NUMBER,
            getToken()
        )
    }
}