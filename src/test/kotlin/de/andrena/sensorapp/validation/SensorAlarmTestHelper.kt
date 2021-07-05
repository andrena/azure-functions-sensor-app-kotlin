package de.andrena.sensorapp.validation

import io.kotest.assertions.show.show
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

infix fun SensorAlarm.shouldBeEquivalentTo(other: SensorAlarm) {
    this should beEquivalentTo(other)
}

fun beEquivalentTo(other: SensorAlarm) = object : Matcher<SensorAlarm> {

    override fun test(value: SensorAlarm): MatcherResult {
        val areEquivalent = value.sensorBoxId == other.sensorBoxId && value.sensorType == other.sensorType &&
                value.status == other.status
        return MatcherResult(
            areEquivalent,
            "${value.show().value} should be equivalent to ${other.show().value}",
            "${value.show().value} should not be equivalent to ${other.show().value}",
        )
    }

}
