package au.com.realestate.hometime.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    private val UNIX_TIME_PATTERN = "(\\d+)\\+(\\d{4})".toRegex()
    private val TIME_FORMATTER = SimpleDateFormat("d MMMM h:m a")
    private const val MILLISECONDS_IN_A_MINUTE = 60000

    fun timeFromUnixTime(dateString: String): Long? {
        return UNIX_TIME_PATTERN
            .find(dateString)?.groups?.get(1)?.value?.toLong()
    }

    fun timeDifferenceFromNowInMinutes(timeInMilliseconds: Long): Int {
        val currentTimeInMilliseconds = Calendar.getInstance().timeInMillis
        val timeDifference: Long =
            (timeInMilliseconds - currentTimeInMilliseconds) / MILLISECONDS_IN_A_MINUTE
        return timeDifference.toInt()
    }

    fun dateString(timeInMilliseconds: Long): String {
        val date = Date(timeInMilliseconds)
        return TIME_FORMATTER.format(date)
    }

    fun formattedDisplay(minutes: Int): String {
        return when {
            minutes == 0 -> "NOW"
            minutes in (1..59) -> "$minutes min"
            minutes > 59 -> "${minutes / 60} hr"
            else -> "N/A"
        }
    }
}