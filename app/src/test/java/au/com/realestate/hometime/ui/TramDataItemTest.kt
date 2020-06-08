package au.com.realestate.hometime.ui

import au.com.realestate.hometime.mocks.MockTimeUtils
import au.com.realestate.hometime.models.Tram
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class TramDataItemTest {
    lateinit var mockTimeUtils: MockTimeUtils

    @org.junit.Before
    fun setUp() {
        mockTimeUtils = MockTimeUtils()
    }

    @org.junit.Test
    fun `TramDataItem tramId is set using tram vehicleNo when vehicleNo has 4 digits`() {
        // Given
        val vehicleNo = 1234
        val mockTram = Tram(
            destination = "",
            predictedArrival = "",
            routeNo = "0",
            vehicleNo = vehicleNo
        )

        // When
        val subject = HomeTimeDataItem.TramDataItem(mockTram, mockTimeUtils)

        // Then
        assertEquals("#1234", subject.tramId)
    }

    @org.junit.Test
    fun `TramDataItem tramId is set using tram vehicleNo with padded 0s when vehicleNo has less than 4 digits`() {
        // Given
        val vehicleNo = 1
        val mockTram = Tram(
            destination = "",
            predictedArrival = "",
            routeNo = "0",
            vehicleNo = vehicleNo
        )

        // When
        val subject = HomeTimeDataItem.TramDataItem(mockTram, mockTimeUtils)

        // Then
        assertEquals("#0001", subject.tramId)
    }

    @org.junit.Test
    fun `TramDataItem tramRoute is set to tram tramRoute`() {
        // Given
        val mocktramRoute = "246"
        val mockTram = Tram(
            destination = "",
            predictedArrival = "",
            routeNo = mocktramRoute,
            vehicleNo = 0
        )

        // When
        val subject = HomeTimeDataItem.TramDataItem(mockTram, mockTimeUtils)

        // Then
        assertEquals(mocktramRoute, subject.tramRoute)
    }

    @org.junit.Test
    fun `TramDataItem destination is set to tram destination`() {
        // Given
        val mockDestination = "Mock Destination"
        val mockTram = Tram(
            destination = mockDestination,
            predictedArrival = "",
            routeNo = "0",
            vehicleNo = 0
        )

        // When
        val subject = HomeTimeDataItem.TramDataItem(mockTram, mockTimeUtils)

        // Then
        assertEquals(mockDestination, subject.destination)
    }

    fun `TramDataItem displayArrivalTime is set using tram predictedArrival and timeUtils`() {
        // Given
        val mockPredictedArrival = "/Date(1591614001371+1000)/"

        val mockedTimeInMilliSeconds = 1591614001371
        mockTimeUtils.timeInMillisecondsFromUnixTimeStub = mockedTimeInMilliSeconds

        val mockedDisplayTime = "5 mins"
        mockTimeUtils.formattedTimeDifferenceFromCurrentTimeStub = mockedDisplayTime
        val mockTram = Tram(
            destination = "",
            predictedArrival = mockPredictedArrival,
            routeNo = "0",
            vehicleNo = 0
        )

        // When
        val subject = HomeTimeDataItem.TramDataItem(mockTram, mockTimeUtils)

        // Then
        assertTrue(mockTimeUtils.timeInMillisecondsFromUnixTimeHasBeenCalled)
        assertEquals(
            mockPredictedArrival,
            mockTimeUtils.timeInMillisecondsFromUnixTimeHasBeenCalledWith
        )
        assertTrue(mockTimeUtils.formattedTimeDifferenceFromCurrentTimeHasBeenCalled)
        assertEquals(
            mockedTimeInMilliSeconds,
            mockTimeUtils.formattedTimeDifferenceFromCurrentTimeCalledWith
        )
        assertEquals(mockedDisplayTime, subject.displayArrivalTime)
    }

    fun `TramDataItem arrivalDate is set using tram predictedArrival and timeUtils`() {
        // Given
        val mockPredictedArrival = "/Date(1591614001372+1000)/"

        val mockedTimeInMilliSeconds = 1591614001372
        mockTimeUtils.timeInMillisecondsFromUnixTimeStub = mockedTimeInMilliSeconds

        val mockedFormattedDate = "6 Jun 8:30 PM"
        mockTimeUtils.formattedDateTimeStringStub = mockedFormattedDate
        val mockTram = Tram(
            destination = "",
            predictedArrival = mockPredictedArrival,
            routeNo = "0",
            vehicleNo = 0
        )

        // When
        val subject = HomeTimeDataItem.TramDataItem(mockTram, mockTimeUtils)

        // Then
        assertTrue(mockTimeUtils.timeInMillisecondsFromUnixTimeHasBeenCalled)
        assertEquals(
            mockPredictedArrival,
            mockTimeUtils.timeInMillisecondsFromUnixTimeHasBeenCalledWith
        )
        assertTrue(mockTimeUtils.formattedDateTimeStringHasBeenCalled)
        assertEquals(mockedTimeInMilliSeconds, mockTimeUtils.formattedDateTimeStringCalledWith)
        assertEquals(mockedFormattedDate, subject.arrivalDate)
    }
}
