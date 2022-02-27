package com.example.weather_forecast.view

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weather_forecast.R
import com.example.weather_forecast.databinding.MainActivityBinding
import com.example.weather_forecast.view.details.DETAILS_INTENT_FILTER

class MainActivity : AppCompatActivity() {
    private lateinit var binding:MainActivityBinding
    private val mainReceiver = MainBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)

        registerReceiver(mainReceiver, IntentFilter(NETWORK_STATE_CHANGED_ACTION))

        val view = binding.root
        setContentView(view)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onDestroy() {

        unregisterReceiver(mainReceiver)
        super.onDestroy()
    }
}