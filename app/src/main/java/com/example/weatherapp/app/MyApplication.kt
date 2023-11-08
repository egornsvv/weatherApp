package com.example.weatherapp.app

import android.app.Application
import com.example.weatherapp.di.AppComponent
import com.example.weatherapp.di.AppModule
import com.example.weatherapp.di.DaggerAppComponent
import com.example.weatherapp.di.NetworkModule
import com.example.weatherapp.di.ViewModelModule

class MyApplication : Application() {

    lateinit var  appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .viewModelModule(ViewModelModule())
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .build()
    }
}