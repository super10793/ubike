package com.demo.ubike.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.library.baseAdapters.BR
import com.demo.ubike.R
import com.demo.ubike.data.viewmodel.MapViewModel
import com.demo.ubike.databinding.FragmentMapBinding
import com.demo.ubike.extension.permission.PermissionCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@SuppressLint("MissingPermission")
@AndroidEntryPoint
class MapFragment : BasePermissionFragment<FragmentMapBinding, MapViewModel>(), OnMapReadyCallback,
    LocationListener, OnCameraIdleListener {
    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var map: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var locationManager: LocationManager
    private val MIN_TIME: Long = 400
    private val MIN_DISTANCE = 1000f

    private val markers: MutableList<MarkerCheck> = mutableListOf()

    override fun getViewModelClass(): Class<MapViewModel> = MapViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_map

    override val bindingVariable: Int = BR.mapViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        viewModel.fetchStationAndInsert()
    }

    override fun initObserver() {
        viewModel.stations.observe(viewLifecycleOwner) { stations ->
            if (!::map.isInitialized) return@observe
            if (stations == null || stations.isEmpty()) return@observe
            markers.forEach { marker -> marker.checked = false }
            stations.forEach { entity ->
                val target = markers.firstOrNull { it.id == entity.stationUID }
                if (target == null) {
                    val pos = LatLng(
                        entity.positionLat,
                        entity.positionLon
                    )
                    val iconBitmap = BitmapFactory.decodeResource(resources, R.drawable.marker)
                    val width = 100
                    val height = 100
                    val scaledBitmap = Bitmap.createScaledBitmap(iconBitmap, width, height, true)
                    map.addMarker(
                        MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap))
                            .position(pos)
                    )?.also { marker ->
                        marker.tag = entity
                        markers.add(
                            MarkerCheck(
                                id = entity.stationUID,
                                marker = marker,
                                checked = true
                            )
                        )
                    }
                } else {
                    target.checked = true
                }
            }

            markers.removeAll { item ->
                if (item.checked) {
                    false
                } else {
                    item.marker.remove()
                    true
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnCameraIdleListener(this)
        changeMapDefaultUi()
        moveToCurrentLocation()
    }

    override fun onLocationChanged(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16f)
        map.moveCamera(cameraUpdate)
        map.isMyLocationEnabled = true
        viewModel.getStations(location.latitude, location.longitude)
        locationManager.removeUpdates(this)
    }

    override fun onCameraIdle() {
        val cameraPosition = map.cameraPosition
        val target = cameraPosition.target
        val latitude = target.latitude
        val longitude = target.longitude
        viewModel.getStations(latitude, longitude)
    }

    private fun moveToCurrentLocation() {
        requestPermissions(
            permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            rationale = null,
            requestCode = 900,
            permissionCallback = object : PermissionCallback {
                override fun onPermissionGranted(perms: MutableList<String>) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME,
                        MIN_DISTANCE,
                        this@MapFragment
                    )
                }

                override fun onPermissionDenied(perms: MutableList<String>) {
                    val sydney = LatLng(-34.0, 151.0)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16f))
                }
            }
        )
    }

    private fun changeMapDefaultUi() {
        // 調整「我當前位置」按鈕位置，設定在右下角
        val mapView = mapFragment.requireView()
        val myLocationButtonTagName = "GoogleMapMyLocationButton"
        val locationButton = mapView.findViewWithTag<View>(myLocationButtonTagName)
        locationButton?.let {
            val layoutParams = it.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.setMargins(0, 0, 30, 30)
        }

        // 點marker時隱藏導航按鈕
        map.uiSettings.isMapToolbarEnabled = false
    }

    data class MarkerCheck(
        var id: String,
        var marker: Marker,
        var checked: Boolean
    )
}