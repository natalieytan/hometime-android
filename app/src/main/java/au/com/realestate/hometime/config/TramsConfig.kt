package au.com.realestate.hometime.config

import au.com.realestate.hometime.models.TramStop

object TramsConfig {
    const val TRAM_NUMBER = 78

    val TRAM_STOPS = listOf(
        TramStop(4055, "North Tram Stop"),
        TramStop(4155, "South Tram Stop")
    )
}