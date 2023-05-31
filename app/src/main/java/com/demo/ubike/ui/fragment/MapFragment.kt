package com.demo.ubike.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.viewModels
import com.demo.ubike.R
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.viewmodel.HomeViewModel
import com.demo.ubike.data.viewmodel.MapViewModel
import com.demo.ubike.databinding.FragmentMapBinding
import com.demo.ubike.extension.permission.PermissionCallback
import com.demo.ubike.ui.view.MarkerView
import com.demo.ubike.ui.view.OnStationDetailListener
import com.demo.ubike.ui.view.StationDetailView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import dagger.hilt.android.AndroidEntryPoint


@SuppressLint("MissingPermission")
@AndroidEntryPoint
class MapFragment : BasePermissionFragment<FragmentMapBinding, MapViewModel>(), OnMapReadyCallback,
    LocationListener, OnMarkerClickListener, OnCameraIdleListener, OnMapClickListener,
    OnCameraMoveListener, OnStationDetailListener {

    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var map: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var locationManager: LocationManager
    private val MIN_TIME: Long = 400
    private val MIN_DISTANCE = 1000f
    private val VISIBLE_ZOOM_LEVEL = 12
    private val markers: MutableList<MarkerCheck> = mutableListOf()
    private var lastClickMarker: Marker? = null
    private var selectFromFavorite: FavoriteEntity? = null

    override fun getViewModelClass(): Class<MapViewModel> = MapViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_map

    override val bindingVariable: Int = BR.mapViewModel

    private val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        viewModel.fetchAllStationAndInsert()
    }

    override fun onPause() {
        super.onPause()
        restoreLastMarker()
        viewDataBinding.flStationDetail.removeAllViews()
        selectFromFavorite = null
    }

    override fun initObserver() {
        viewModel.stations.observe(viewLifecycleOwner) { stations ->
            if (!::map.isInitialized) return@observe
            if (stations == null || stations.isEmpty()) return@observe
            markers.forEach { marker -> marker.checked = false }
            stations.forEach { entity ->
                val target = markers.firstOrNull { it.id == entity.stationUID }
                if (target == null) {
                    val pos = LatLng(entity.positionLat, entity.positionLon)
                    val icon = BitmapDescriptorFactory.fromBitmap(getMarkerBitmap(entity, false))
                    map.addMarker(
                        MarkerOptions()
                            .icon(icon)
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

            // auto click marker when favorite fragment click some item
            markers.find {
                it.id == selectFromFavorite?.stationUID
            }?.also {
                onMarkerClick(it.marker)
                selectFromFavorite = null
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

        homeViewModel.favoriteItemClicked.observe(viewLifecycleOwner) {
            selectFromFavorite = it
            val latLng = LatLng(it.positionLat, it.positionLon)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16f)
            map.moveCamera(cameraUpdate)
        }
    }

    private fun getMarkerBitmap(entity: StationEntity, highlight: Boolean): Bitmap {
        val view = MarkerView(
            context = requireContext(),
            stationEntity = entity,
            needHighlight = highlight
        )
        val iconGenerator = IconGenerator(requireContext())
        iconGenerator.setContentView(view)
        iconGenerator.setBackground(null)
        return iconGenerator.makeIcon()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMarkerClickListener(this)
        map.setOnMapClickListener(this)
        map.setOnCameraMoveListener(this)
        map.setOnCameraIdleListener(this)
        changeMapDefaultUi()
        moveToCurrentLocation()
    }

    override fun onLocationChanged(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16f)
        map.moveCamera(cameraUpdate)
        map.isMyLocationEnabled = true
        locationManager.removeUpdates(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as? StationEntity
        val detailView = viewDataBinding.flStationDetail.findViewWithTag<View>(tag?.stationUID)

        // 如果重複點擊同個標記，就不需要再次addView
        if (tag != null && detailView == null) {
            val highlightIcon = BitmapDescriptorFactory.fromBitmap(getMarkerBitmap(tag, true))
            marker.setIcon(highlightIcon)
            restoreLastMarker()
            lastClickMarker = marker

            val customView = StationDetailView(
                context = requireContext(),
                station = tag,
                listener = this
            )
            customView.tag = tag.stationUID
            viewDataBinding.flStationDetail.removeAllViews()
            viewDataBinding.flStationDetail.addView(customView)
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_up)
            customView.clearAnimation()
            customView.startAnimation(animation)
            viewModel.fetchStationDetail(tag.city, tag.stationUID)
        }

        return false
    }


    override fun onCameraIdle() {
        val zoom = map.cameraPosition.zoom
        val target = map.cameraPosition.target
        val latitude = target.latitude
        val longitude = target.longitude
        if (zoom > VISIBLE_ZOOM_LEVEL) {
            viewModel.getStations(latitude, longitude)
        } else {
            map.clear()
            markers.clear()
            lastClickMarker = null
            viewDataBinding.flStationDetail.removeAllViews()
        }
    }

    override fun onCameraMove() {
        viewModel.cancelGetStations()
    }

    override fun onMapClick(latLng: LatLng) {
        viewDataBinding.flStationDetail.removeAllViews()
        restoreLastMarker()
    }

    override fun onStationDetailClose() {
        restoreLastMarker()
    }

    private fun restoreLastMarker() {
        lastClickMarker?.apply {
            val tag = this.tag as? StationEntity
            tag?.let {
                val icon = BitmapDescriptorFactory.fromBitmap(getMarkerBitmap(tag, false))
                setIcon(icon)
            }
            lastClickMarker = null
        }
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

        // 禁止旋轉地圖
        map.uiSettings.isRotateGesturesEnabled = false
    }

    data class MarkerCheck(
        var id: String,
        var marker: Marker,
        var checked: Boolean
    )
}