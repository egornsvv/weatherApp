package com.example.weatherapp.di

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.model.DailyWeatherData
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.presentation.viewmodels.ViewModelFactory
import com.example.weatherapp.presentation.viewmodels.WeatherViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import kotlin.reflect.KClass

@Module
class ViewModelModule {

    @Provides
    fun provideWeatherViewModel(repository: WeatherRepository): WeatherViewModel {
        return WeatherViewModel(repository)
    }

    @Provides
    fun provideViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel> >): ViewModelProvider.Factory {
        return ViewModelFactory(creators)
    }
    @Provides
    fun provideSharedViewModel(repository: WeatherRepository): WeatherViewModel {
        return WeatherViewModel(repository)
    }

    @Provides
    fun provideDataByDateLiveData(): MutableLiveData<MutableMap<String, MutableList<DailyWeatherData>>> {
        return MutableLiveData()
    }

    @Provides
    fun provideViewModelMap(repository: WeatherRepository): Map<Class<out ViewModel>, Provider<ViewModel>> {
        val viewModelMap = mutableMapOf<Class<out ViewModel>, Provider<ViewModel>>()
        viewModelMap[WeatherViewModel::class.java] = Provider<ViewModel> { provideWeatherViewModel(repository
        ) }
        return viewModelMap
    }
    @MustBeDocumented
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ViewModelKey(val value: KClass<WeatherViewModel>)
}