package com.example.weather_forecast.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather_forecast.R
import com.example.weather_forecast.databinding.FragmentMainBinding
import com.example.weather_forecast.model.WeatherInfo
import com.example.weather_forecast.view.details.DetailsFragment
import com.example.weather_forecast.viewmodel.AppState
import com.example.weather_forecast.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment() : Fragment() {
    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var isDataSetRus: Boolean = true

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemViewClickListener {
        override fun onItemViewClick(weatherInfo: WeatherInfo) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA,weatherInfo)
                    }))
                    .addToBackStack("")
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

        viewModel.apply {
            getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
            getWeatherFromLocalSourceRus()
        }
    }

    private fun renderData(appState:AppState) {
        when(appState) {
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                binding.mainFragmentRootView.showSnackBar(
                    getStingByName("error"),
                    getStingByName("reload"),
                    { viewModel.getWeatherFromLocalSourceRus() }
                )
            }
        }
    }

    private fun changeWeatherDataSet() {

        if (!isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }
        isDataSetRus = !isDataSetRus

    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener()
        _binding = null
    }

    private fun View.showSnackBar(
        text:String,
        actionText: String,
        action:(View) -> Unit,
        length:Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }

    private fun Fragment.getStingByName(name : String) =
        resources.getIdentifier(name, "string", context?.packageName).getStringRes()

    private fun Int.getStringRes() = getString(this)

}