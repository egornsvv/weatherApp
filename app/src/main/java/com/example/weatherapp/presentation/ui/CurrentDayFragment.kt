package com.example.weatherapp.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.app.MyApplication
import com.example.weatherapp.data.model.DailyWeatherData
import com.example.weatherapp.databinding.FragmentCurrentDayBinding
import com.example.weatherapp.presentation.adapter.WeatherDateAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CurrentDayFragment : Fragment() {

    private var _binding: FragmentCurrentDayBinding? = null
    private val binding get() = _binding!!
    private val bottomNavigationView by lazy {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentDayBinding.inflate(inflater, container, false)

        val weatherDataByDateJson = arguments?.getString("weatherDataByDate")

        if (weatherDataByDateJson != null) {
            val gson = Gson()
            val weatherDataByDate: MutableMap<String, MutableList<DailyWeatherData>> =
                gson.fromJson(weatherDataByDateJson, object : TypeToken<MutableMap<String, MutableList<DailyWeatherData>>>() {}.type)

            val allWeatherData = weatherDataByDate.values.flatten()
            val adapter = WeatherDateAdapter(allWeatherData)

            with(binding.rc) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                this.adapter = adapter
            }
        }
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mainFragment -> {
                    findNavController().popBackStack()
                    true
                }
                else -> false
            }
        }
        return binding.root
    }
}