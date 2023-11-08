package com.example.weatherapp.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.app.MyApplication
import com.example.weatherapp.data.model.DailyWeatherData
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.presentation.adapter.WeatherAdapter
import com.example.weatherapp.presentation.viewmodels.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val weatherViewModel: WeatherViewModel by viewModels { viewModelFactory }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val apiKey = "4b3253950ac6914c9a29957f4c2cdeb2"
    private val units = "metric"
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private val weatherAdapter: WeatherAdapter by lazy { WeatherAdapter() }

    private var lat: Double = 0.0
    private var lon: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        recyclerView = binding.rc
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = weatherAdapter
        weatherViewModel.weatherResponse.observe(viewLifecycleOwner) { response ->
            val weatherDataList = weatherViewModel.weatherData.value
            weatherDataList?.let { weatherAdapter.submitList(it) }
            binding.textView.text = response?.city?.name
        }

        weatherAdapter.setOnItemClickListener(object : WeatherAdapter.OnWeatherItemClickListener {
            override fun onWeatherItemClick(weatherData: DailyWeatherData) {
                val currentDate = weatherData.date
                val response = weatherViewModel.weatherResponse.value
                response?.let { weatherViewModel.processWeatherByDate(currentDate, it) }
                val gson = Gson()
                val weatherDataByDateJson = gson.toJson(weatherViewModel.weatherDataByDate.value)
                val bundle = Bundle().apply {
                    putString("weatherDataByDate", weatherDataByDateJson)
                }
                findNavController().navigate(R.id.currentDayFragment, bundle)
            }
        })

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            getLocation()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getLocation()
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLocation() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            lat = location?.latitude ?: 0.0
            lon = location?.longitude ?: 0.0
            if (lat != 0.0 && lon != 0.0) {
                getWeatherData(lat, lon)
            }
        }
    }


    private fun getWeatherData(latitude: Double, longitude: Double) {
        lifecycleScope.launch {
            try {
                val response = weatherViewModel.getWeather(latitude, longitude, apiKey, units)
                weatherViewModel.weatherResponse.value = response
                weatherViewModel.processWeatherData(response)
                binding.textView.text = response.city.name
                weatherViewModel.weatherData.observe(viewLifecycleOwner) { weatherDataList ->
                    weatherAdapter.submitList(weatherDataList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}