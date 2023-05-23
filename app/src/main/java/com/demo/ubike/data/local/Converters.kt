package com.demo.ubike.data.local

import androidx.room.TypeConverter
import com.demo.ubike.data.model.City
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun toCityJson(city: City): String {
        return Gson().toJson(city, City::class.java)
    }

    @TypeConverter
    fun fromCityObject(json: String): City {
        return Gson().fromJson(json, City::class.java)
    }
}