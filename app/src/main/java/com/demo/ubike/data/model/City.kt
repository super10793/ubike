package com.demo.ubike.data.model

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

enum class City(
    val apiKey: String,
    val nickname: String,
    val cityCenter: LatLng
) {
    Taipei(
        apiKey = "Taipei",
        nickname = "TPE",
        LatLng(25.047763, 121.517088)
    ),
    NewTaipei(
        apiKey = "NewTaipei",
        nickname = "NWT",
        LatLng(25.008141, 121.464298)
    ),
    Taoyuan(
        apiKey = "Taoyuan",
        nickname = "TAO",
        LatLng(24.990334, 121.313756)
    ),
    Hsinchu(
        apiKey = "Hsinchu",
        nickname = "HSZ",
        LatLng(24.803041, 120.968278)
    ),
    HsinchuCounty(
        apiKey = "HsinchuCounty",
        nickname = "HSQ",
        LatLng(24.835535485802463, 121.0092678666115)
    ),
    Miaoli(
        apiKey = "MiaoliCounty",
        nickname = "MIA",
        LatLng(24.567701430750656, 120.82176502794027)
    ),
    Taichung(
        apiKey = "Taichung",
        nickname = "TXG",
        LatLng(24.136829, 120.685040)
    ),
    Chiayi(
        apiKey = "Chiayi",
        nickname = "CYI",
        LatLng(22.669167, 120.485278)
    ),
    Kaohsiung(
        apiKey = "Kaohsiung",
        nickname = "KHH",
        LatLng(22.639548, 120.302877)
    ),
    Tainan(
        apiKey = "Tainan",
        nickname = "TNN",
        LatLng(22.99647114137429, 120.21282445639372)
    ),
    Pingtung(
        apiKey = "PingtungCounty",
        nickname = "PIF",
        LatLng(22.669167, 120.485278)
    ),

    Changhua(
        apiKey = "ChanghuaCounty",
        nickname = "CHA",
        LatLng(24.07522, 120.54407)
    ),
    Yunlin(
        apiKey = "YunlinCounty",
        nickname = "YUN",
        LatLng(23.695069, 120.525417)
    ),
    Taitung(
        apiKey = "TaitungCounty",
        nickname = "TTT",
        LatLng(22.7551, 121.1503)
    ),
    ChiayiCounty(
        apiKey = "ChiayiCounty",
        nickname = "CYQ",
        LatLng(23.4592, 120.29395)
    );

    companion object {
        fun fromValue(value: String): City {
            return entries.first { it.nickname.lowercase() == value.lowercase() }
        }
    }
}