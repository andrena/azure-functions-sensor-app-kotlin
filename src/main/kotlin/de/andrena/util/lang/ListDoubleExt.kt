package de.andrena.util.lang

import kotlin.math.pow
import kotlin.math.sqrt

fun List<Double>.averageOrNull(): Double? =
    average().run { if (this.isNaN()) null else this }

fun List<Double>.stdDev(): Double? {
    if (this.isEmpty()) {
        return null
    }

    val avg = this.average()
    val squaredDeviations = this.map { it - avg }.map { it.pow(2) }
    val variance: Double = squaredDeviations.sum() / this.size
    return sqrt(variance)
}
