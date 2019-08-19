package com.twitter.challenge;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView temperatureView = findViewById(R.id.temperature);
        final TextView windSpeedView = findViewById(R.id.wind_speed);
        final ImageView cloudinessView = findViewById(R.id.cloudiness);
        final TextView deviationView = findViewById(R.id.deviation);

        final WeatherViewModel model = ViewModelProviders.of(this).get(WeatherViewModel.class);

        Observer<WeatherConditions> currentWeatherObserver = new Observer<WeatherConditions>() {
            @Override
            public void onChanged(WeatherConditions weatherConditions) {
                windSpeedView.setText(String.format("%f", weatherConditions.getWind().getSpeed()));
                Log.d(TAG, "Cloudiness: " + weatherConditions.getClouds().getCloudiness());
                if(weatherConditions.getClouds().getCloudiness() > 50) { // TODO Move this and log to ViewModel
                    cloudinessView.setBackground(getResources().getDrawable(R.mipmap.ic_launcher));
                }
                temperatureView.setText(getString(R.string.temperature, weatherConditions.getWeather().getTemp(), TemperatureConverter.celsiusToFahrenheit( (float)weatherConditions.getWeather().getTemp())));
                // TODO hide getTemp() behind ViewModel
            }
        };
        model.getLiveCurrentWeather().observe(this, currentWeatherObserver);

        Observer<Double> tempDeviationObserver = new Observer<Double>() {
            @Override
            public void onChanged(Double deviation) {
                // TODO Add a button to fetch this instead of loading it onCreate
                deviationView.setText(deviation+"");
            }
        };
        model.getLiveFiveDayTempDeviation().observe(this, tempDeviationObserver);

    }

}
//    Coding Exercise for Twitter
//        Please take no more than 3 hours to complete the following:
//        Suppose we need a simple Android app to display the weather:
//        1. Use the provided starter code to get started. 2. Use the API to fetch the current weather. 3. Display the following current conditions:
//        a. Temperature in Celsius and Fahrenheit b. com.twitter.challenge.Wind speed. c. A cloud icon if the cloudiness percentage is greater than 50%. 4. Provide a button, that when tapped, fetches the weather for the next 5 days, and displays
//        the standard deviation of the temperature, whose formula is provided below: s tddev = √ i=1 ∑nn−1 (x i
//        −x) 2
//        n = number of data points x = average/mean of the data xi = each of the values of the data ∑ is the summation operator which is defined as the addition of all the elements of the series. In this case is the addition of (x i − x ) 2 afor each temperature in the series Because this is a brief exercise and not a full fledged app, you can make the following assumptions:
//        ● Persisting data across multiple app launches isn’t required.
//        ● Refreshing the data while the app is running isn’t required.
//        Additional instructions:
//        ● We know making a great-looking UI takes a lot longer than 3 hours. Your UI should be functional and responsive, but is not expected to be attractive. You will be judged on the quality of your code, not the style of your UI.
//        ● You must be able to handle configuration and orientation changes without hitting the network again.
//        ● The app should not crash under any circumstance, but robust error handling is not necessary. Logging exceptions is sufficient.
//        ● Consider what would happen if the user’s network speed is slow.
//        ● Feel free to use any public third-party libraries
//        ● Test the core functionality.
