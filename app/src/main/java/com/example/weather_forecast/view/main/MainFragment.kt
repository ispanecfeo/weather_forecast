package com.example.weather_forecast.view.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather_forecast.R
import com.example.weather_forecast.databinding.FragmentMainBinding
import com.example.weather_forecast.model.CityInfo
import com.example.weather_forecast.utils.showSnackBar
import com.example.weather_forecast.view.details.DetailsFragment
import com.example.weather_forecast.view.map.MapFragment
import com.example.weather_forecast.viewmodel.AppState
import com.example.weather_forecast.viewmodel.MainViewModel
import com.example.weather_forecast.viewmodel.MainViewModelFactory


private const val FIRST_OPEN = "first_open"
private const val REQUEST_CODE = 12345

class MainFragment() : Fragment() {
    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var isDataSetRus: Boolean = true
    private var lat: Double = 0.0
    private var lon: Double = 0.0

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

        override fun onGeoButtonClick(cityInfo: CityInfo) {
            lat = cityInfo.lat
            lon = cityInfo.lon
            checkPermission()
        }
    })

    private fun checkPermission() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    openMapFragment()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun openMapFragment() {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .add(
                    R.id.container,
                    MapFragment.newInstance(Bundle().apply {
                        putDouble(MapFragment.BUNDLE_LAT, lat)
                        putDouble(MapFragment.BUNDLE_LON, lon)
                    })
                )
                .addToBackStack("")
                .commit()
        }
    }

    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.dialog_rationale_meaasge))
                .setPositiveButton(getString(R.string.dialog_rationale_give_access)) { _, _ ->
                    requestPermission()
                }
                .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun checkPermissionsResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                var grantedPermissions = 0

                if ((grantResults.isNotEmpty())) {
                    for (i in grantResults) {
                        if (i == PackageManager.PERMISSION_GRANTED) {
                            grantedPermissions++
                        }

                        if (grantResults.size == grantedPermissions) {
                            openMapFragment()
                        } else {
                            showDialog(
                                getString(R.string.dialog_title_no_gps),
                                getString(R.string.dialog_message_no_gps)
                            )
                        }
                    }
                } else {
                    showDialog(
                        getString(R.string.dialog_title_no_gps),
                        getString(R.string.dialog_message_no_gps)
                    )
                }

                return
            }
        }
    }

    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        checkPermissionsResult(requestCode, grantResults)
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

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

                it.getPreferences(Context.MODE_PRIVATE).edit().putBoolean(FIRST_OPEN, false).apply()

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