package au.com.realestate.hometime.ui

import au.com.realestate.hometime.models.TramStop
import org.junit.Assert.*

class TramStopHeaderItemTest {
    @org.junit.Test
    fun `TramStopHeaderItem stopName is set using tramStop name`() {
        // Given
        val mockTramStopName = "West Trams"
        val mockTramStop = TramStop(
            id = 0,
            name = mockTramStopName
        )

        // When
        val subject = HomeTimeDataItem.TramStopHeaderItem(mockTramStop)

        // Then
        assertEquals(mockTramStopName, subject.stopName)
    }
}