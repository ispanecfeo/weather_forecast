package com.example.weather_forecast.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weather_forecast.R
import com.example.weather_forecast.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }


    }
}