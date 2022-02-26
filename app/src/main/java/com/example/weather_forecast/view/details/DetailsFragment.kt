package com.example.weather_forecast.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.weather_forecast.R
import com.example.weather_forecast.databinding.FragmentDetailsBinding
import com.example.weather_forecast.model.WeatherInfo
import ru.gb.kotlinapp.model.WeatherInfoDTO


class DetailsFragment : Fragment() {

    private var _binding:FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: WeatherInfo

    private val onLoadListener: WeatherInfoLoader.WeatherInfoLoaderListener =
        object : WeatherInfoLoader.WeatherInfoLoaderListener {

            override fun onLoaded(weatherDTO: WeatherInfoDTO) {
                displayWeather(weatherDTO)
            }

            override fun onFailed(throwable: Throwable) {
                Toast.makeText(requireContext(), "Ошибка загрузки", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun displayWeather(weatherDTO: WeatherInfoDTO) {
        with (binding) {
            main.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE

            val cityInfo = weatherBundle.cityInfo

            cityName.text = cityInfo.city
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                cityInfo.lat.toString(),
                cityInfo.lon.toString()
            )

            temperatureConditional.text = weatherDTO.fact?.condition
            temperatureValue.text = weatherDTO.fact?.temp.toString().degreeOfCelsius()
            feelsLikeValue.text = weatherDTO.fact?.feels_like.toString().degreeOfCelsius()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherBundle = arguments?.getParcelable<WeatherInfo>(BUNDLE_EXTRA) ?: WeatherInfo()

        with(binding) {
            main.visibility = View.GONE
            loadingLayout.visibility = View.VISIBLE
        }

        val loader = WeatherInfoLoader(
            onLoadListener,
            weatherBundle.cityInfo.lat,
            weatherBundle.cityInfo.lon
        )

        loader.loadWeather()

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