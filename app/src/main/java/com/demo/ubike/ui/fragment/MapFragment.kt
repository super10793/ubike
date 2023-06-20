package com.demo.ubike.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.viewModels
import com.demo.ubike.R
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.CustomMarker
import com.demo.ubike.data.model.CustomMarkerOption
import com.demo.ubike.data.viewmodel.HomeViewModel
import com.demo.ubike.data.viewmodel.MapViewModel
import com.demo.ubike.databinding.FragmentMapBinding
import com.demo.ubike.extension.permission.PermissionCallback
import com.demo.ubike.extension.view.getStatusBarHeight
import com.demo.ubike.ui.view.MarkerView
import com.demo.ubike.ui.view.OnStationDetailListener
import com.demo.ubike.ui.view.StationDetailView
import com.demo.ubike.ui.view.SupportCityView
import com.demo.ubike.utils.FirebaseAnalyticsUtil
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
import javax.inject.Inject


@SuppressLint("MissingPermission")
@AndroidEntryPoint
class MapFragment : BasePermissionFragment<FragmentMapBinding, MapViewModel>(), OnMapReadyCallback,
    LocationListener, OnMarkerClickListener, OnCameraIdleListener, OnMapClickListener,
    OnCameraMoveListener, OnStationDetailListener {

    companion object {
        fun newInstance() = MapFragment()
    }

    @Inject
    lateinit var firebaseAnalyticsUtil: FirebaseAnalyticsUtil

    private lateinit var map: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var locationManager: LocationManager
    private val MIN_TIME: Long = 400
    private val MIN_DISTANCE = 1000f
    private val VISIBLE_ZOOM_LEVEL = 12
    private val CAMERA_ZOOM_LEVEL = 15f
    private val CAMERA_ZOOM_LEVEL_CONTAIN_TAIWAN = 8.1f
    private val TAIWAN_LATLNG: LatLng = LatLng(23.617133617023338, 121.0056421905756)

    private val pendingMarkers: MutableList<CustomMarkerOption> = mutableListOf()
    private val currentMarkers: MutableList<CustomMarker> = mutableListOf()
    private var selectFromFavorite: FavoriteEntity? = null
    private val iconCache = hashMapOf<Pair<Int, Boolean>, Bitmap>()
    private var userIsMoving = false
    private var lastClickMarker: Marker? = null

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
        initSearchButton()
    }

    override fun onResume() {
        super.onResume()
        val isMapOk = this::map.isInitialized
        when {
            isMapOk && hasAnyLocationPermissions() -> {
                map.isMyLocationEnabled = true
            }

            isMapOk && !hasAnyLocationPermissions() -> {
                map.isMyLocationEnabled = false
            }

            else -> {}
        }
    }

    override fun onPause() {
        super.onPause()
        restoreLastMarker()
        viewDataBinding.flStationDetail.removeAllViews()
        selectFromFavorite = null
        onSearchBtnClick(false)
    }

    override fun initObserver() {
        viewModel.stations.observe(viewLifecycleOwner) { stations ->
            if (!::map.isInitialized) return@observe
            if (stations == null || stations.isEmpty()) return@observe

            // 移除不該出現在地圖上的標記
            val iterator = currentMarkers.iterator()
            while (iterator.hasNext()) {
                val current = iterator.next()
                val find = stations.find { current.id == it.stationUID }
                if (find == null) {
                    current.marker.remove()
                    iterator.remove()
                }
            }

            /*
            * 如果最後一次點擊的marker即將要被移除
            * 1.將`flStationDetail`內的view全部移除
            * 2.將`lastClickMarker`還原
            * */
            val findLastMarker = stations.find {
                val tag = lastClickMarker?.tag as? StationEntity
                tag?.stationUID == it.stationUID
            }
            if (findLastMarker == null) {
                lastClickMarker = null
                viewDataBinding.flStationDetail.removeAllViews()
            }

            pendingMarkers.clear()
            stations.forEach { entity ->
                val pos = LatLng(entity.positionLat, entity.positionLon)
                val icon = BitmapDescriptorFactory.fromBitmap(getMarkerBitmap(entity, false))
                val option = MarkerOptions().icon(icon).position(pos)
                pendingMarkers.add(CustomMarkerOption(entity.stationUID, option, entity))
            }

            startAddMarker()
        }

        homeViewModel.favoriteItemClicked.observe(viewLifecycleOwner) {
            selectFromFavorite = it
            val latLng = LatLng(it.positionLat, it.positionLon)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, CAMERA_ZOOM_LEVEL)
            map.moveCamera(cameraUpdate)
        }
    }

    private fun startAddMarker() {
        while (pendingMarkers.isNotEmpty() && !userIsMoving) {
            val first = pendingMarkers.first()
            val alreadyOnMap = currentMarkers.find { it.id == first.id } != null
            if (!alreadyOnMap) {
                map.addMarker(first.markerOptions)?.also {
                    it.tag = first.stationEntity
                    currentMarkers.add(CustomMarker(first.stationEntity.stationUID, it))
                }
            }

            // auto click marker when favorite fragment click some item
            selectFromFavorite?.let { favorite ->
                currentMarkers.find {
                    it.id == favorite.stationUID
                }?.also {
                    onMarkerClick(it.marker)
                    selectFromFavorite = null
                }
            }

            pendingMarkers.removeFirst()
        }
    }

    private fun getMarkerBitmap(entity: StationEntity, highlight: Boolean): Bitmap {
        val cache = iconCache[Pair(entity.serviceType, highlight)]
        /*
        * 產生Bitmap目前只需要`entity.serviceType`加上`highlight`變數
        * `serviceType`只有5種可能，`highlight`有2種可能
        * 因所有組合不多，所以將產生過的icon存起來，如果有就直接回傳使用:)
        * */
        if (cache != null) return cache

        val view = MarkerView(
            context = requireContext(),
            stationEntity = entity,
            needHighlight = highlight
        )
        val iconGenerator = IconGenerator(requireContext())
        iconGenerator.setContentView(view)
        iconGenerator.setBackground(null)
        val result = iconGenerator.makeIcon()
        iconCache[Pair(entity.serviceType, highlight)] = result
        return result
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
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, CAMERA_ZOOM_LEVEL)
        map.moveCamera(cameraUpdate)
        map.isMyLocationEnabled = true
        locationManager.removeUpdates(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as? StationEntity
        val detailView = viewDataBinding.flStationDetail.findViewWithTag<View>(tag?.stationUID)

        // 如果重複點擊同個標記，就不需要再次addView
        if (tag != null && detailView == null) {
            firebaseAnalyticsUtil.markerClickEvent(tag.stationUID, tag.stationNameZhTw)
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
        userIsMoving = false
        val zoom = map.cameraPosition.zoom
        val target = map.cameraPosition.target
        val latitude = target.latitude
        val longitude = target.longitude
        if (zoom > VISIBLE_ZOOM_LEVEL) {
            viewModel.getStations(latitude, longitude)
        } else {
            map.clear()
            pendingMarkers.clear()
            currentMarkers.clear()
            lastClickMarker = null
            viewDataBinding.flStationDetail.removeAllViews()
        }

        startAddMarker()
    }

    override fun onCameraMove() {
        userIsMoving = true
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
        val lastMarkerUid = (lastClickMarker?.tag as? StationEntity)?.stationUID
        val target = currentMarkers.firstOrNull { it.id == lastMarkerUid }
        target?.apply {
            val tag = this.marker.tag as? StationEntity
            tag?.let {
                val icon = BitmapDescriptorFactory.fromBitmap(getMarkerBitmap(tag, false))
                this.marker.setIcon(icon)
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
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            TAIWAN_LATLNG,
                            CAMERA_ZOOM_LEVEL_CONTAIN_TAIWAN
                        )
                    )
                    onSearchBtnClick(true)
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

    private fun initSearchButton() {
        // set margin
        val layoutParams = viewDataBinding.flSearch.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.setMargins(
            layoutParams.marginStart,
            requireActivity().getStatusBarHeight() ?: 120,
            layoutParams.rightMargin,
            layoutParams.bottomMargin
        )
        viewDataBinding.flSearch.layoutParams = layoutParams


        // set listener
        viewDataBinding.flSearch.setOnClickListener {
            val count = viewDataBinding.flSupportCity.childCount
            val expandIt = (count == 0)
            onSearchBtnClick(expandIt)
        }
    }

    private fun onSearchBtnClick(isExpand: Boolean) {
        if (isExpand) {
            // set icon
            viewDataBinding.ivSearch.setImageResource(R.drawable.close)

            // add view
            val customView = SupportCityView(
                context = requireContext(),
                hasFullLocationPermission = hasFullLocationPermissions(),
                onCityClick = {
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        it.cityCenter,
                        CAMERA_ZOOM_LEVEL
                    )
                    map.moveCamera(cameraUpdate)
                    firebaseAnalyticsUtil.supportCityClickEvent(it.apiKey)
                },
                onGoToSettingClick = {
                    gotToAndroidSettings()
                    firebaseAnalyticsUtil.goToSettingClick()
                }
            )
            val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.expand_search)
            viewDataBinding.flSupportCity.addView(customView)
            customView.startAnimation(anim)
        } else {
            // set icon
            viewDataBinding.ivSearch.setImageResource(R.drawable.search)

            // remove view
            val child = viewDataBinding.flSupportCity.getChildAt(0)
            child?.let {
                val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.collapse_search)
                anim.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        // nothing
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        viewDataBinding.flSupportCity.removeView(it)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                        // nothing
                    }
                })
                it.startAnimation(anim)
            }
        }
    }

    private fun gotToAndroidSettings() {
        val uri = Uri.parse("package:${requireContext().packageName}")
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        requireContext().startActivity(intent)
    }
}