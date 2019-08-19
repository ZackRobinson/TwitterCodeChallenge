package com.twitter.challenge;

import android.os.Bundle;
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
        model.loadCurrentWeatherConditions();
        model.loadFutureWeatherConditions();

        Observer<Float> currentTempObserver = new Observer<Float>() {
            @Override
            public void onChanged(Float currentTemp) {
                temperatureView.setText(getString(R.string.temperature, currentTemp, TemperatureConverter.celsiusToFahrenheit(currentTemp)));
            }
        };

        Observer<Double> currentWindSpeedObserver = new Observer<Double>() {
            @Override
            public void onChanged(Double currentWindSpeed) {
                windSpeedView.setText(String.format("%f", currentWindSpeed));
            }
        };

        Observer<Boolean> currentCloudinessObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isCloudy) {
                if (isCloudy)
                    cloudinessView.setBackground(getResources().getDrawable(R.mipmap.ic_launcher));
            }
        };

        Observer<Float> tempDeviationObserver = new Observer<Float>() {
            @Override
            public void onChanged(Float deviation) {
                // TODO Add a button to fetch this instead of loading it onCreate
                deviationView.setText(deviation + "");
            }
        };

        model.getCurrentTemp().observe(this, currentTempObserver);
        model.getCurrentWindSpeed().observe(this, currentWindSpeedObserver);
        model.isCloudy().observe(this, currentCloudinessObserver);
        model.getFiveDayTempDeviation().observe(this, tempDeviationObserver);

    }

}
