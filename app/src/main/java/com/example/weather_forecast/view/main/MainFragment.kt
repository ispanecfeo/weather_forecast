package com.example.weather_forecast.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather_forecast.R
import com.example.weather_forecast.databinding.FragmentMainBinding
import com.example.weather_forecast.model.CityInfo
import com.example.weather_forecast.model.WeatherInfo
import com.example.weather_forecast.utils.showSnackBar
import com.example.weather_forecast.view.details.DetailsFragment
import com.example.weather_forecast.viewmodel.AppState
import com.example.weather_forecast.viewmodel.MainViewModel
import com.example.weather_forecast.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*

private const val FIRST_OPEN = "first_open"

class MainFragment() : Fragment() {
    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var isDataSetRus: Boolean = true

    interface ListenerBottomNavigatorMenu {
        fun onCloseMenu()
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory())
            .get(MainViewModel::class.java)
    }

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemViewClickListener {
        override fun onItemViewClick(cityInfo: CityInfo) {
            (requireContext() as ListenerBottomNavigatorMenu).onCloseMenu()

            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, cityInfo)
                    }))
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
             }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            mainFragmentRecyclerView.adapter = adapter
            mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        }

        viewModel.getLiveData()
            .observe(
                viewLifecycleOwner, Observer
                { renderData(it) }
            )

        showListOfTowns(isDataSetRus)
    }

    private fun showListOfTowns(isRussian : Boolean) {
        activity?.let {
            if (it.getPreferences(Context.MODE_PRIVATE).getBoolean(FIRST_OPEN, true)) {

                viewModel.setCitiesIntoDB()

                it.getPreferences(Context.MODE_PRIVATE).edit().putBoolean(FIRST_OPEN, false).commit()

                if (isRussian)
                    viewModel.getWeatherFromLocalSourceRus(false)
                else
                    viewModel.getWeatherFromLocalSourceWorld(false)
            } else {
                if (isRussian)
                    viewModel.getWeatherFromLocalSourceRus(true)
                else
                    viewModel.getWeatherFromLocalSourceWorld(true)
            }
        }
    }

    private fun renderData(appState:AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setCities(appState.citiesData)
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                binding.mainFragmentRootView.showSnackBar(
                    getStingByName("error"),
                    getStingByName("reload"),
                    { showListOfTowns(isDataSetRus) }
                )
            }
        }
    }

    private fun changeWeatherDataSet() {

        if (!isDataSetRus) {
            showListOfTowns(false)
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        } else {
            showListOfTowns(true)
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }
        isDataSetRus = !isDataSetRus

    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener()
        _binding = null
    }

    private fun Fragment.getStingByName(name : String) =
        resources.getIdentifier(name, "string", context?.packageName).getStringRes()

    private fun Int.getStringRes() = getString(this)

}