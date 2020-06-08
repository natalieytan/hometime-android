package au.com.realestate.hometime.mocks

import au.com.realestate.hometime.utils.TimeUtility

class MockTimeUtils : TimeUtility {
    var timeInMillisecondsFromUnixTimeStub: Long? = null
    var timeInMillisecondsFromUnixTimeHasBeenCalled = false
    var timeInMillisecondsFromUnixTimeHasBeenCalledWith: String? = null
    override fun timeInMillisecondsFromUnixTime(dateString: String): Long? {
        timeInMillisecondsFromUnixTimeHasBeenCalled = true
        timeInMillisecondsFromUnixTimeHasBeenCalledWith = dateString
        return timeInMillisecondsFromUnixTimeStub
    }

    var formattedDateTimeStringStub: String = ""
    var formattedDateTimeStringHasBeenCalled: Boolean = false
    var formattedDateTimeStringCalledWith: Long? = null
    override fun formattedDateTimeString(timeInMilliseconds: Long): String {
        formattedDateTimeStringCalledWith = timeInMilliseconds
        formattedDateTimeStringHasBeenCalled = true
        return formattedDateTimeStringStub
    }

    var formattedCurrentDateTimeStringStub = ""
    var formattedCurrentDateTimeStringHasBeenCalled: Boolean = false
    override fun formattedCurrentDateTimeString(): String {
        formattedCurrentDateTimeStringHasBeenCalled = true
        return formattedCurrentDateTimeStringStub
    }

    var formattedTimeDifferenceFromCurrentTimeStub: String = ""
    var formattedTimeDifferenceFromCurrentTimeHasBeenCalled = false
    var formattedTimeDifferenceFromCurrentTimeCalledWith: Long? = null
    override fun formattedTimeDifferenceFromCurrentTime(timeInMilliseconds: Long): String {
        formattedTimeDifferenceFromCurrentTimeCalledWith = timeInMilliseconds
        formattedTimeDifferenceFromCurrentTimeHasBeenCalled = true
        return formattedTimeDifferenceFromCurrentTimeStub
    }
}