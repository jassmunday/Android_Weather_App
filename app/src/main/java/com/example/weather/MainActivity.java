package com.example.weather;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import api.WeatherService;
import model.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private TextView locationTextView;
    private ImageView weatherIcon;
    private TextView temperatureTextView;

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "787d523b6c266a5c7394738612ea9f31"; // Replace with your actual API key
    private static final String UNITS = "metric"; // Use "imperial" for Fahrenheit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        locationTextView = findViewById(R.id.locationTextView);
        weatherIcon = findViewById(R.id.weatherIcon);
        temperatureTextView = findViewById(R.id.temperatureTextView);

        // Example city: London
        // String cityName = "Ludhiana";

        findViewById(R.id.buttonGetWeather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get city name from EditText
                String cityName = editTextCity.getText().toString();

                // Initialize Retrofit
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // Create a WeatherService instance
                WeatherService weatherService = retrofit.create(WeatherService.class);

                // Make the API call
                Call<WeatherResponse> call = weatherService.getWeather(cityName, API_KEY, UNITS);
                call.enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        if (response.isSuccessful()) {
                            WeatherResponse weatherResponse = response.body();
                            if (weatherResponse != null) {
                                updateWeatherUI(cityName, weatherResponse);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    private void updateWeatherUI(String location, WeatherResponse weatherResponse) {
        // Update UI with weather data
        locationTextView.setText("Location: " + location);
        temperatureTextView.setText("Temperature: " + weatherResponse.getMain().getTemp() + "°C");

        // Use Picasso library to load weather icon from URL
        String iconCode = weatherResponse.getWeather()[0].getIcon();
        String iconUrl = "http://openweathermap.org/img/w/" + iconCode + ".png";
        Picasso.get().load(iconUrl).into(weatherIcon);
    }
}