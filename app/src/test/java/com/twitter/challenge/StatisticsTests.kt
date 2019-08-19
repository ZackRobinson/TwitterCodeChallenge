package com.twitter.challenge

import org.assertj.core.api.Java6Assertions.assertThat
import org.assertj.core.api.Java6Assertions.within

import org.junit.Test

class StatisticsTests {
    @Test
    fun testTemperatureDeviation() {
        val precision = within(0.01)
        val data = listOf(11.0, 10.0, 10.0, 20.0, 20.0, 20.0)
        assertThat(TemperatureStatistics.standardDeviation(data).also{println(it)}).isEqualTo(5.30, precision)
    }

    @Test
    fun testTemperatureDeviation2() {
        val precision = within(0.01)
        val data = listOf(16.83, 14.2, 9.88, 19.19, 11.15)
        assertThat(TemperatureStatistics.standardDeviation(data).also{println(it)}).isEqualTo(3.45, precision)
    }
}
