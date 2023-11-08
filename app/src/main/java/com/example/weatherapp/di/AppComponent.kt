package com.example.weatherapp.di

import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.app.MyApplication
import com.example.weatherapp.presentation.ui.CurrentDayFragment
import com.example.weatherapp.presentation.ui.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(application: MyApplication)
    fun inject(app: MainFragment)
    fun inject(app: CurrentDayFragment)
    fun viewModelFactory(): ViewModelProvider.Factory

}