package com.example.weather_forecast.view

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.weather_forecast.R
import com.example.weather_forecast.databinding.MainFragmentBinding
import com.example.weather_forecast.model.WeatherInfo
import com.example.weather_forecast.viewmodel.AppState
import com.example.weather_forecast.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    private var _binding:MainFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val observe =  Observer<AppState>{
            processingData(it)
        }
        viewModel.getLiveData().observe(viewLifecycleOwner, observe)
        viewModel.getWeatherFromLocalSource()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun processingData(appState: AppState) {

        when(appState) {
            is AppState.Success -> {
                val weatherData = appState.weatherData
                binding.loadingLayout.visibility = View.GONE

                Snackbar.make(
                    binding.main,
                    "Success",
                    Snackbar.LENGTH_LONG
                ).show()

                setData(weatherData)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE

                Snackbar
                    .make(binding.main, "Error", Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.reload)) {
                        viewModel.getWeatherFromLocalSource()
                    }
                    .show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(weatherData:WeatherInfo) {
        binding.cityName.text = weatherData.cityInfo.city
        binding.temperatureValue.text = weatherData.temperature.toString() + getString(R.string.degree_of_celsius)
        binding.temperatureConditional.text = weatherData.condition
    }
}