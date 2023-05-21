package com.demo.ubike.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import com.demo.ubike.R
import com.demo.ubike.data.viewmodel.MapViewModel
import com.demo.ubike.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@SuppressLint("MissingPermission")
@AndroidEntryPoint
class MapFragment : BasePermissionFragment<FragmentMapBinding, MapViewModel>(), OnMapReadyCallback {
    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var map: GoogleMap
    override fun getViewModelClass(): Class<MapViewModel> = MapViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_map

    override val bindingVariable: Int = BR.mapViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun initObserver() {}

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}