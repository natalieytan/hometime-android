package au.com.realestate.hometime.models

import com.squareup.moshi.Json

data class Token(
    @Json(name = "DeviceToken")
    val value: String? = null
)