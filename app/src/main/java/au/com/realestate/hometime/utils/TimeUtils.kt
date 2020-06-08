package au.com.realestate.hometime.utils

import java.text.SimpleDateFormat
import java.util.*

interface TimeUtility {
    fun timeInMillisecondsFromUnixTime(dateString: String): Long?
    fun formattedDateTimeString(timeInMilliseconds: Long): String
    fun formattedCurrentDateTimeString(): String
    fun formattedTimeDifferenceFromCurrentTime(timeInMilliseconds: Long): String

}

object TimeUtils : TimeUtility {
    private val UNIX_TIME_PATTERN = "(\\d+)\\+(\\d{4})".toRegex()
    private val TIME_FORMATTER = SimpleDateFormat("d MMMM h:mm a")
    private const val MILLISECONDS_IN_A_MINUTE = 60000

    override fun timeInMillisecondsFromUnixTime(dateString: String): Long? {
        return UNIX_TIME_PATTERN
            .find(dateString)?.groups?.get(1)?.value?.toLong()
    }

    override fun formattedDateTimeString(timeInMilliseconds: Long): String {
        val date = Date(timeInMilliseconds)
        return TIME_FORMATTER.format(date)
    }

    override fun formattedCurrentDateTimeString(): String {
        return formattedDateTimeString(currentTimeInMilliSeconds())
    }

    override fun formattedTimeDifferenceFromCurrentTime(timeInMilliseconds: Long): String {
        val minutes = minutesDifferenceFromCurrentTime(timeInMilliseconds)
        return formattedTimeDifference(minutes)
    }

    private fun currentTimeInMilliSeconds(): Long {
        return Calendar.getInstance().timeInMillis
    }

    private fun minutesDifferenceFromCurrentTime(timeInMilliseconds: Long): Int {
        val timeDifference: Long =
            (timeInMilliseconds - currentTimeInMilliSeconds()) / MILLISECONDS_IN_A_MINUTE
        return timeDifference.toInt()
    }

    private fun formattedTimeDifference(minutes: Int): String {
        return when {
            minutes == 0 -> "NOW"
            minutes in (1..59) -> "$minutes min"
            minutes > 59 -> "${minutes / 60} hr"
            else -> "N/A"
        }
    }
}