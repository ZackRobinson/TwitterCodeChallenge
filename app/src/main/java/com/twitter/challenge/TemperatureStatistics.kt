package com.twitter.challenge

import kotlin.math.pow
import kotlin.math.sqrt

class TemperatureStatistics {
    companion object {
        fun standardDeviation(data: List<Double>): Double {
            val average = data.sum().div(data.size)
            return sqrt(data.map { temp -> (temp - average).pow(2.0) }.sum()
                                                                .div
                                                            (data.size)                 )
        }
    }

}