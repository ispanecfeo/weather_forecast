package com.example.weather_forecast.view.history.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather_forecast.R
import com.example.weather_forecast.databinding.FragmentDetailHistoryBinding
import com.example.weather_forecast.view.config.KEY_ONLY_WARM
import com.example.weather_forecast.viewmodel.AppState
import com.example.weather_forecast.viewmodel.DetailHistoryViewModel

class DetailHistoryFragment: Fragment() {

    private var _binding: FragmentDetailHistoryBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val BUNDLE_EXTRA_LAT = "current_lat"
        const val BUNDLE_EXTRA_LON = "current_lon"
        const val BUNDLE_EXTRA_CITY = "city";
        fun newInstance(bundle: Bundle): DetailHistoryFragment {
            val fragment = DetailHistoryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val viewModel: DetailHistoryViewModel by lazy {
        ViewModelProvider(this)
            .get(DetailHistoryViewModel::class.java)
    }

    private val adapter = HistoryDetailFragmentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.historyRecyclerView.adapter = adapter

        val city: String = arguments?.getString(BUNDLE_EXTRA_CITY, "") as String
        val lat: Double =  arguments?.getDouble(BUNDLE_EXTRA_LAT, 0.0) as Double
        val lot: Double =  arguments?.getDouble(BUNDLE_EXTRA_LON, 0.0) as Double

        viewModel.getLiveData().observe(viewLifecycleOwner){
            renderData(it)
        }

        with(binding) {
            historyCity.text = city
            historyCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                lat.toString(),
                lot.toString()
            )
        }

        val aboveZero: Boolean =
            activity?.getPreferences(Context.MODE_PRIVATE)?.getBoolean(KEY_ONLY_WARM, false) ?: false

        viewModel.getHistoryData(lat, lot, aboveZero)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessHistoryWeather -> {
                appState.data?.let {
                    adapter.setData(it)
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}