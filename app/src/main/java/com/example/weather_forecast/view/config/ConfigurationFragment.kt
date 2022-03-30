package com.example.weather_forecast.view.config

import android.content.ContentProvider
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weather_forecast.databinding.FragmentConfigurationBinding

public const val KEY_ONLY_WARM = "key_only_warm"

class ConfigurationFragment:Fragment() {
    private var _binding: FragmentConfigurationBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ConfigurationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            binding.onlyWarm.isChecked =
                it.getPreferences(Context.MODE_PRIVATE)
                    .getBoolean(KEY_ONLY_WARM, false)
        }

        binding.onlyWarm.setOnCheckedChangeListener {
                _,
                isChecked ->  activity?.let {

                    it.getPreferences(Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean(KEY_ONLY_WARM, isChecked)
                        .apply()
            }
        }
    }



}