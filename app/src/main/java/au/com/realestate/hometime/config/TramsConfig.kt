package au.com.realestate.hometime.config

import au.com.realestate.hometime.HomeTimeApplication
import au.com.realestate.hometime.R
import au.com.realestate.hometime.models.TramStop

object TramsConfig {
    const val TRAM_NUMBER = 78

    val TRAM_STOPS = listOf(
        TramStop(4055, HomeTimeApplication.context.getString(R.string.north_tram_stop)),
        TramStop(4155, HomeTimeApplication.context.getString(R.string.south_tram_stop))
    )
}