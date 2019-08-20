package com.twitter.challenge

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class WeatherViewModel : ViewModel() {

    private val TAG = this.javaClass.simpleName

    private val weatherApi by lazy { WeatherApi.create() }

    val currentTemp: MutableLiveData<Float> by lazy { MutableLiveData<Float>() }
    val currentWindSpeed: MutableLiveData<Double> by lazy { MutableLiveData<Double>() }
    val isCloudy: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val fiveDayTempDeviation: MutableLiveData<Float> by lazy { MutableLiveData<Float>() }

    private var currentWeatherDisposable: Disposable? = null
    private var futureWeatherDisposable: Disposable? = null

    fun loadCurrentWeatherConditions() {
        currentWeatherDisposable = weatherApi.getCurrentWeather().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(5)
            .subscribe({ conditions ->
                Log.d(TAG, conditions.toString())
                this.currentTemp.value = conditions.weather.temp.toFloat()
                this.isCloudy.value = conditions.clouds.cloudiness > 50
                this.currentWindSpeed.setValue(conditions.wind.speed)
            }, { Log.d(TAG, "loadCurrentWeatherConditions: $it") })
    }

    fun loadFutureWeatherConditions() {
        val listOfFutureTemps = mutableListOf<Double>()

        val observables = mutableListOf<Observable<WeatherConditions>>()
        for (i in 1..5) {
            observables
                .add(
                    weatherApi.getFutureWeather(i)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).retry(5)
                )
        }

        futureWeatherDisposable = Observable.merge(observables).subscribe({ conditions ->
            listOfFutureTemps.add(conditions.weather.temp
                .also { println("Future Temperature: $it List: $listOfFutureTemps") })
                .also {
                    fiveDayTempDeviation.setValue(
                        TemperatureStatistics.standardDeviation(listOfFutureTemps).toFloat()
                            .also { println("5 day Deviation: $it") })
                }
        }, { Log.d(TAG, "loadFutureWeatherConditions: $it") })

    }

}
