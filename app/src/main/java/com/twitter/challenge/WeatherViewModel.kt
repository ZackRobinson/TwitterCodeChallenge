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

    val liveCurrentWeather: MutableLiveData<WeatherConditions> by lazy {
        MutableLiveData<WeatherConditions>().also { loadCurrentWeatherConditions() }
    }

    val liveFiveDayTempDeviation: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>().also { loadFutureWeatherConditions() }
    }

    private val weatherApi by lazy {
        WeatherApi.create()
    }

    private var currentWeatherDisposable: Disposable? = null
    private var futureWeatherDisposable: Disposable? = null

    private fun loadCurrentWeatherConditions() {
        currentWeatherDisposable = weatherApi.getCurrentCatalog().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { conditions ->
                this.liveCurrentWeather.setValue(conditions)
                    .also { Log.d(TAG, conditions.toString()) }
            }

    }

    private fun loadFutureWeatherConditions() {
        var listOfFutureTemps = mutableListOf<Double>()

        val observables = listOf( // TODO create this list dynamically
            weatherApi.getFutureCatalog(1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()),
            weatherApi.getFutureCatalog(2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()),
            weatherApi.getFutureCatalog(3).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()),
            weatherApi.getFutureCatalog(4).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()),
            weatherApi.getFutureCatalog(5).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()))

        futureWeatherDisposable = Observable.merge(observables).subscribe { conditions ->
            listOfFutureTemps.add(conditions.weather.temp.also{ println("Future Temperature: $it List: $listOfFutureTemps" )})
                .also { liveFiveDayTempDeviation.setValue(TemperatureStatistics.standardDeviation(listOfFutureTemps).also{ println("5 day Deviation: $it")}) }
        }

    }
}
