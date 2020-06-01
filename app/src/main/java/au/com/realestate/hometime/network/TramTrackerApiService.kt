package au.com.realestate.hometime.network

import au.com.realestate.hometime.models.ApiResponse
import au.com.realestate.hometime.models.Token
import au.com.realestate.hometime.models.Tram
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://ws3.tramtracker.com.au/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

data class ApiStatus(val state: State, val message: String?) {
    enum class State { LOADING, ERROR, DONE, CLEARED }
}

interface TramTrackerApiService {
    @GET("TramTracker/RestService/GetDeviceToken/?aid=TTIOSJSON&devInfo=HomeTimeAndroid")
    suspend fun getToken(): ApiResponse<Token>

    @GET("TramTracker/RestService/GetNextPredictedRoutesCollection/{stopId}/{tramNo}/false/?aid=TTIOSJSON&cid=2")
    suspend fun getTrams(@Path("stopId") stopId: Int, @Path("tramNo") tramNo: Int, @Query("tkn") token: String): ApiResponse<Tram>
}

object TramTrackerApi {
    val retrofitService: TramTrackerApiService by lazy {
        retrofit.create(TramTrackerApiService::class.java)
    }
}