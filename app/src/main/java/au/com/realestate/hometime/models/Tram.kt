package au.com.realestate.hometime.models

import com.squareup.moshi.Json

data class Tram(
    @Json(name = "Destination")
    val destination: String? = null,
    @Json(name = "PredictedArrivalDateTime")
    val predictedArrival: String? = null,
    @Json(name = "RouteNo")
    val routeNo: String? = null,
    @Json(name= "VehicleNo")
    val vehicleNo: Int? = null
)