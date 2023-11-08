package com.example.weatherapp.data.model

import android.os.Parcel
import android.os.Parcelable

data class DailyWeatherData(
    val date: String,
    var minTemperature: Double,
    var maxTemperature: Double,
    val cloudiness: Int,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble()
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<DailyWeatherData> {
        override fun createFromParcel(parcel: Parcel): DailyWeatherData {
            return DailyWeatherData(parcel)
        }

        override fun newArray(size: Int): Array<DailyWeatherData?> {
            return arrayOfNulls(size)
        }
    }
}