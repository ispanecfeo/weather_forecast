package com.example.weather_forecast.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.weather_forecast.App
import com.example.weather_forecast.R
import com.example.weather_forecast.databinding.MainActivityBinding
import com.example.weather_forecast.view.config.ConfigurationFragment
import com.example.weather_forecast.view.history.HistoryFragment
import com.example.weather_forecast.view.main.MainFragment
import com.yandex.mapkit.MapKitFactory


class MainActivity : AppCompatActivity(), MainFragment.ListenerBottomNavigatorMenu {
    private lateinit var binding: MainActivityBinding
    private val key = "2c08b69e-78b6-488e-bb65-4b7915eb58f7"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(key)
        binding = MainActivityBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)


        val setFragment : Boolean = (savedInstanceState == null)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.loadWeather -> setCurrentFragment(MainFragment.newInstance())
                R.id.history -> setCurrentFragment(HistoryFragment.newInstance())
                R.id.settings -> setCurrentFragment(ConfigurationFragment.newInstance())
            }
            true
        }

        val numberFragment = getPreferences(Context.MODE_PRIVATE)
            .getInt(App.NUMBER_FRAGMENT, 1)

        when(numberFragment) {
            1 -> setCurrentFragment(MainFragment.newInstance(), setFragment)
            2 -> setCurrentFragment(HistoryFragment.newInstance(), setFragment)
            3 -> setCurrentFragment(ConfigurationFragment.newInstance(), setFragment)
        }
    }

    private fun setCurrentFragment(fragment: Fragment, setFragment: Boolean = true) {
        if (setFragment) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, fragment)
                commitNow()
            }
        }
    }

    override fun onCloseMenu() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (binding.bottomNavigationView.visibility == View.GONE) {
            binding.bottomNavigationView.visibility = View.VISIBLE
        }
        super.onBackPressed()
    }



}
