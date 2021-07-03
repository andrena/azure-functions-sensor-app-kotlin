package de.andrena.sensorapp.sensor

import io.kotest.assertions.show.show
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

infix fun Sensor.shouldBeEquivalentTo(other: Sensor) {
    this should beEquivalentTo(other)
}

fun beEquivalentTo(other: Sensor) = object : Matcher<Sensor> {

    override fun test(value: Sensor): MatcherResult {
        val areEquivalent = value.sensorBoxId == other.sensorBoxId && value.type == other.type &&
                value.min == other.min && value.max == other.max
        return MatcherResult(
            areEquivalent,
            "${value.show().value} should be equivalent to ${other.show().value}",
            "${value.show().value} should not be equivalent to ${other.show().value}",
        )
    }

}
