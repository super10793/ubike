package com.demo.ubike.ui.view

interface OnStationDetailListener {
    fun onStationDetailClose()
    fun onGoToGoogleMapClick(lat: Double, lon: Double)
}