package com.example.weather_forecast.view.history

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather_forecast.R
import com.example.weather_forecast.databinding.FragmentHistoryBinding
import com.example.weather_forecast.view.details.DetailsFragment
import com.example.weather_forecast.view.history.detail.DetailHistoryFragment
import com.example.weather_forecast.viewmodel.AppState
import com.example.weather_forecast.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this)
            .get(HistoryViewModel::class.java)
    }

    private val adapter = HistoryFragmentAdapter() { historyCity ->

        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .add(R.id.container, DetailHistoryFragment.newInstance(Bundle().apply {
                    putString(DetailHistoryFragment.BUNDLE_EXTRA_CITY, historyCity.city)
                    putDouble(DetailHistoryFragment.BUNDLE_EXTRA_LAT, historyCity.lat)
                    putDouble(DetailHistoryFragment.BUNDLE_EXTRA_LON, historyCity.lon)

                }))
                .addToBackStack("")
                .commit()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding){
            historyFragmentRecyclerview.adapter = adapter
            searchCity.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setDataToModel()
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })
        }

        viewModel.getLiveData().observe(
            viewLifecycleOwner
        ) { renderData(it) }



        setDataToModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun renderData(appState: AppState?) {

        if (appState != null) {

            when (appState) {
                is AppState.SuccessHistoryData -> {
                    appState.data?.let { adapter.setData(it) }
                }
            }

        }
    }

    private fun setDataToModel() {
        viewModel.getHistoryData(getSearchString())
    }


    private fun getSearchString(): String {
        return if (binding.searchCity.text.toString().trim() == "") "%"
        else binding.searchCity.text.toString().trim() + "%"
    }

}