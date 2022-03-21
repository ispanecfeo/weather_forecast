package com.example.weather_forecast.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weather_forecast.R
import com.example.weather_forecast.databinding.FragmentDetailsBinding
import com.example.weather_forecast.model.WeatherInfo
import ru.gb.kotlinapp.model.WeatherInfoDTO

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_FACT_EXTRA = "DETAILS FACT EXTRA"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
private const val TEMP_INVALID = -100
private const val FEELS_LIKE_INVALID = -100
private const val PROCESS_ERROR = "Обработка ошибки"

class DetailsFragment : Fragment() {

    private var _binding:FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: WeatherInfo

    private val weatherInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when(intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {

                DETAILS_RESPONSE_SUCCESS_EXTRA -> intent.getParcelableExtra<WeatherInfoDTO>(
                    DETAILS_FACT_EXTRA
                )
                    ?.let { weatherBundle ->
                        displayWeather(weatherBundle)
                    }

                DETAILS_RESPONSE_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)

            }

        }

    }


    private val onLoadListener: WeatherInfoLoader.WeatherInfoLoaderListener =
        object : WeatherInfoLoader.WeatherInfoLoaderListener {

            override fun onLoaded(weatherDTO: WeatherInfoDTO) {
                displayWeather(weatherDTO)
            }

            override fun onFailed(throwable: Throwable) {
                Toast.makeText(requireContext(), "Ошибка загрузки", Toast.LENGTH_LONG).show()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(weatherInfoReceiver, IntentFilter(DETAILS_INTENT_FILTER))
        }
    }


    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .unregisterReceiver(weatherInfoReceiver)
        }

        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
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
        getWeatherInfoFromService()

    }

    private fun getWeatherInfoFromService() {
        with(binding) {
            main.visibility = View.GONE
            loadingLayout.visibility = View.VISIBLE
        }

        context?.let {
            it.startService(
                Intent(it, WeatherInfoService::class.java).apply {
                    putExtra(WEATHER_INFO_EXTRA, weatherBundle)
                }
            )
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