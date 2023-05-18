package com.demo.ubike.data.model

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

enum class City(
    val apiKey: String,
    private val latLngBounds: LatLngBounds
) {
    Taipei(
        "Taipei", LatLngBounds(
            LatLng(24.3963, 121.4346),
            LatLng(25.3016, 122.0067)
        )
    ),
    NewTaipei(
        "NewTaipei", LatLngBounds(
            LatLng(24.7269, 121.2828),
            LatLng(25.2917, 122.0512)
        )
    ),
    Taoyuan(
        "Taoyuan", LatLngBounds(
            LatLng(24.5455, 121.0279),
            LatLng(25.1441, 121.4758)
        )
    ),
    Hsinchu(
        "Hsinchu", LatLngBounds(
            LatLng(24.7915, 120.9365),
            LatLng(24.8646, 121.0234)
        )
    ),
    HsinchuCounty(
        "HsinchuCounty", LatLngBounds(
            LatLng(23.8744, 120.7066),
            LatLng(24.8449, 121.2926)
        )
    ),
    Miaoli(
        "MiaoliCounty", LatLngBounds(
            LatLng(24.3096, 120.6197),
            LatLng(24.6999, 121.2815)
        )
    ),
    Taichung(
        "Taichung", LatLngBounds(
            LatLng(24.0259, 120.4428),
            LatLng(24.3103, 120.8162)
        )
    ),
    Chiayi(
        "Chiayi", LatLngBounds(
            LatLng(24.0259, 120.4428),
            LatLng(24.3103, 120.8162)
        )
    ),
    Kaohsiung(
        "Kaohsiung", LatLngBounds(
            LatLng(21.8533, 120.1069),
            LatLng(22.9656, 120.7023)
        )
    ),
    Tainan(
        "Tainan", LatLngBounds(
            LatLng(22.7918, 120.0759),
            LatLng(23.4113, 120.4659)
        )
    ),
    Pingtung(
        "PingtungCounty", LatLngBounds(
            LatLng(21.5203, 120.3194),
            LatLng(22.5519, 121.8788)
        )
    ),
    Kinmen(
        "KinmenCounty", LatLngBounds(
            LatLng(24.3854, 118.0962),
            LatLng(24.6101, 118.4931)
        )
    );

    fun currentCity(lat: Double, lng: Double): City {
        val location = LatLng(lat, lng)
        return values().firstOrNull {
            it.latLngBounds.contains(location)
        } ?: Taichung
    }
}