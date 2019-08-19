package com.twitter.challenge;

data class WeatherConditions(
    val name: String,
    val weather: Weather,
    val wind: Wind,
    val clouds: Clouds
)