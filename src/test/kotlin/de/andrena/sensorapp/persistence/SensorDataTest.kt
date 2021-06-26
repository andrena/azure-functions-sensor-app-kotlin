package de.andrena.sensorapp.persistence

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

internal class SensorDataTest {

    @Test
    fun `synchronizes timestamp with super class`() {
        val timestamp = OffsetDateTime.of(2021, 6, 26, 7, 0, 0, 0, ZoneOffset.UTC)
        val data = SensorData("test", "Temperature", "Mean", 15.0)
        data.createdAt = timestamp

        data.timestamp shouldBe Date.from(timestamp.toInstant())

        data.timestamp = Date.from(timestamp.plusDays(3).toInstant())

        data.createdAt shouldBe timestamp.plusDays(3)
    }

}