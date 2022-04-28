package com.example.weather_forecast.view.details

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.weather_forecast.R
import com.example.weather_forecast.databinding.FragmentDetailsBinding
import com.example.weather_forecast.model.CityInfo
import com.example.weather_forecast.model.WeatherInfo
import com.example.weather_forecast.model.getDefaultCity
import com.example.weather_forecast.utils.showSnackBar
import com.example.weather_forecast.viewmodel.AppState
import com.example.weather_forecast.viewmodel.DetailsViewModel
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou


const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_FACT_EXTRA = "DETAILS FACT EXTRA"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"

class DetailsFragment : Fragment() {

    private var _binding:FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var cityBundle: CityInfo

    private val detailsViewModel : DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         cityBundle = arguments?.getParcelable<CityInfo>(BUNDLE_EXTRA) ?: getDefaultCity()

        detailsViewModel
            .detailsLiveData
            .observe(viewLifecycleOwner, Observer { renderData(it) })

        requestWeather()
    }

    private fun displayWeather(weather: WeatherInfo) {

         with (binding) {
            main.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE

            detailsViewModel.addHistoryRow(weather)

            val cityInfo = weather.cityInfo

            cityName.text = cityInfo.city
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                cityInfo.lat.toString(),
                cityInfo.lon.toString()
            )

            temperatureConditional.text = weather.condition
            temperatureValue.text = weather.temperature.toString().degreeOfCelsius()
            feelsLikeValue.text = weather.feelsLike.toString().degreeOfCelsius()
            headerIcon.load(getString(R.string.uri_city_picture))

            GlideToVectorYou.justLoadImage(
                activity,
                Uri.parse("https://yastatic.net/weather/i/icons/funky/dark/${weather.icon}.svg"),
                weatherIcon
            )


        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessLoadFromServer -> {
                binding.main.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                displayWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.main.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.main.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                binding.main.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { requestWeather()}
                )
            }
        }
    }

    fun requestWeather() {

        detailsViewModel.getWeatherFromRemoteSource(
            cityBundle.lat,
            cityBundle.lon
        )
    }

    companion object{
        const val BUNDLE_EXTRA = "city"
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun String.degreeOfCelsius() = this + getString(R.string.degree_of_celsius)

}