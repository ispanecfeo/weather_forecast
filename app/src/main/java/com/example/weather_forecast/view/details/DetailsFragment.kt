package com.example.weather_forecast.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weather_forecast.R
import com.example.weather_forecast.databinding.FragmentDetailsBinding
import com.example.weather_forecast.model.WeatherInfo


class DetailsFragment : Fragment() {

    private var _binding:FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<WeatherInfo>(BUNDLE_EXTRA)?.let { weatherInfo ->
            weatherInfo.cityInfo.also { cityInfo ->  
                binding.cityName.text = cityInfo.city
                binding.cityCoordinates.text = String().format(
                    getString(R.string.city_coordinates),
                    cityInfo.lat.toString(),
                    cityInfo.lon.toString()
                )
                binding.temperatureValue.text = weatherInfo.temperature.toString().degreeOfCelsius()
                binding.feelsLikeValue.text = weatherInfo.temperature.toString().degreeOfCelsius()
            }
        }
    }

    companion object{
        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun String.degreeOfCelsius() = this + getString(R.string.degree_of_celsius)

}