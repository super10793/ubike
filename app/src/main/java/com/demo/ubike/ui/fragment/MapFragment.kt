package com.demo.ubike.ui.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.ToastUtils
import com.demo.ubike.R
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.CustomMarker
import com.demo.ubike.data.model.CustomMarkerOption
import com.demo.ubike.data.model.ServiceType
import com.demo.ubike.data.model.vo.FavoriteStationVO
import com.demo.ubike.data.model.vo.StationVO
import com.demo.ubike.data.viewmodel.HomeViewModel
import com.demo.ubike.data.viewmodel.MapViewModel
import com.demo.ubike.databinding.DialogErrorBinding
import com.demo.ubike.databinding.FragmentMapBinding
import com.demo.ubike.extension.view.dpToPx
import com.demo.ubike.extension.view.getStatusBarHeight
import com.demo.ubike.extension.view.gotToAndroidSettings
import com.demo.ubike.extension.view.showRouteInGoogleMap
import com.demo.ubike.result.EventObserver
import com.demo.ubike.ui.view.MarkerView
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


@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback, OnMarkerClickListener,
    OnCameraIdleListener, OnMapClickListener, OnCameraMoveListener {

    companion object {
        private const val MIN_TIME: Long = 400
        private const val MIN_DISTANCE = 1000f
        private const val VISIBLE_ZOOM_LEVEL = 12
        private const val CAMERA_ZOOM_LEVEL = 15f
        private const val CAMERA_ZOOM_LEVEL_CONTAIN_TAIWAN = 8.1f

        private const val TAIWAN_LAT = 23.617133617023338

        private const val TAIWAN_LNG = 121.0056421905756

        private const val SECURITY_ERROR_MESSAGE = "SecurityException happened"

        private const val GOOGLE_MAP_LOCATION_BUTTON_NAME = "GoogleMapMyLocationButton"

        fun newInstance() = MapFragment()
    }

    @Inject
    lateinit var firebaseAnalyticsUtil: FirebaseAnalyticsUtil

    private var googleMap: GoogleMap? = null
    private var supportMapFragment: SupportMapFragment? = null
    private var locationManager: LocationManager? = null

    private val pendingMarkers: MutableList<CustomMarkerOption> = mutableListOf()
    private val currentMarkers: MutableList<CustomMarker> = mutableListOf()
    private var selectFromFavorite: FavoriteStationVO? = null

    private val markerBitmaps = hashMapOf<Pair<ServiceType, Boolean>, Bitmap>()
    private var userIsMoving = false
    private var lastClickMarker: Marker? = null
    private val locationListener = object : LocationListener {

        override fun onProviderEnabled(provider: String) {
            // ignored
        }

        override fun onProviderDisabled(provider: String) {
            moveCameraToTaiwan()
        }

        override fun onLocationChanged(location: Location) {
            val latLng = LatLng(location.latitude, location.longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, CAMERA_ZOOM_LEVEL)
            googleMap?.moveCamera(cameraUpdate)
            googleMap?.updateMyLocationEnabled(true)
            locationManager?.removeUpdates(this)
        }
    }

    private val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() })
    private val viewModel: MapViewModel by viewModels()

    override fun provideLayoutId(): Int = R.layout.fragment_map

    override fun initView() {
        supportMapFragment =
            childFragmentManager.findFragmentById(R.id.fragment_map) as? SupportMapFragment
        supportMapFragment?.getMapAsync(this)
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        viewModel.fetchAllStationAndInsert()
        initSearchButton()
        prepareMarkerBitmaps()
    }

    override fun onResume() {
        super.onResume()
        googleMap?.updateMyLocationEnabled(hasAnyLocationPermissions())
    }

    override fun onPause() {
        super.onPause()
        restoreLastMarker()
        viewDataBinding.flStationDetail.removeAllViews()
        selectFromFavorite = null
        onSearchBtnClick(false)
    }

    override fun initObserver() {
        viewModel.stations.observe(viewLifecycleOwner) { stationVOs ->
            if (googleMap == null || stationVOs.isNullOrEmpty()) return@observe

            // 移除不該出現在地圖上的標記
            val iterator = currentMarkers.iterator()
            while (iterator.hasNext()) {
                val current = iterator.next()
                val find = stationVOs.find { current.id == it.stationUid }
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
            val findLastMarker = stationVOs.find {
                val tag = lastClickMarker?.tag as? StationVO
                tag?.stationUid == it.stationUid
            }
            if (findLastMarker == null) {
                lastClickMarker = null
                viewDataBinding.flStationDetail.removeAllViews()
            }

            pendingMarkers.clear()
            stationVOs.forEach { vo ->
                val pos = LatLng(vo.positionLat, vo.positionLon)
                val icon = BitmapDescriptorFactory.fromBitmap(getMarkerBitmap(vo, false))
                val option = MarkerOptions().icon(icon).position(pos)
                pendingMarkers.add(CustomMarkerOption(vo.stationUid, option, vo))
            }

            startAddMarker()
        }

        viewModel.error.observe(viewLifecycleOwner, EventObserver {
            val binding = DialogErrorBinding.inflate(layoutInflater)
            val dialog = AlertDialog.Builder(requireContext())
                .setCancelable(true)
                .setView(binding.root)
                .create()

            dialog.window?.apply {
                setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

                /* `setDimAmount()`讓背景半透明，但設置後無效，要先`clearFlags()`再`addFlags()`才有效 */
                clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                setDimAmount(0.5f)
            }

            binding.apply {
                tvMessage.text = it
                tvSubmit.setOnClickListener { dialog.dismiss() }
            }

            dialog.show()
        })

        viewModel.stationDetail.observe(viewLifecycleOwner) { vo ->
            val detailView = getStationDetailView() ?: return@observe
            val stationUid = detailView.tag as? String ?: return@observe
            if (stationUid != vo.stationUid) return@observe

            detailView.renderStationDetail(vo)
        }

        viewModel.favoriteState.observe(viewLifecycleOwner) { vo ->
            val detailView = getStationDetailView() ?: return@observe
            val stationUid = detailView.tag as? String ?: return@observe
            if (stationUid != vo.stationUid) return@observe

            detailView.renderFavoriteState(vo)
        }

        homeViewModel.favoriteItemClicked.observe(viewLifecycleOwner) {
            selectFromFavorite = it
            val latLng = LatLng(it.positionLat, it.positionLon)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, CAMERA_ZOOM_LEVEL)
            googleMap?.moveCamera(cameraUpdate)
        }
    }


    private fun startAddMarker() {
        while (pendingMarkers.isNotEmpty() && !userIsMoving) {
            val first = pendingMarkers.first()
            val alreadyOnMap = currentMarkers.find { it.id == first.id } != null
            if (!alreadyOnMap) {
                googleMap?.addMarker(first.markerOptions)?.also {
                    it.tag = first.stationVO
                    currentMarkers.add(CustomMarker(first.stationVO.stationUid, it))
                }
            }

            // auto click marker when favorite fragment click some item
            selectFromFavorite?.let { favorite ->
                currentMarkers.find {
                    it.id == favorite.stationUid
                }?.also {
                    onMarkerClick(it.marker)
                    selectFromFavorite = null
                }
            }

            pendingMarkers.removeAt(0)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        /* setting listener */
        this.googleMap?.setOnMarkerClickListener(this)
        this.googleMap?.setOnMapClickListener(this)
        this.googleMap?.setOnCameraMoveListener(this)
        this.googleMap?.setOnCameraIdleListener(this)

        /* adjust uiSettings */
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.uiSettings.isRotateGesturesEnabled = false

        checkPermissions()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as? StationVO ?: return false
        val detailView = viewDataBinding.flStationDetail.findViewWithTag<View>(tag.stationUid)

        // 如果重複點擊同個標記，就不需要再次addView
        if (detailView != null) return false

        firebaseAnalyticsUtil.markerClickEvent(tag.stationUid, tag.stationNameZhTw)
        val highlightIcon = BitmapDescriptorFactory.fromBitmap(getMarkerBitmap(tag, true))
        marker.setIcon(highlightIcon)

        restoreLastMarker()
        lastClickMarker = marker

        val stationDetailView = StationDetailView(requireContext()).apply {
            this.tag = tag.stationUid
            this.renderStation(tag)
            this.setOnStationDetailListener(object : StationDetailView.OnStationDetailListener {
                override fun onClose() {
                    restoreLastMarker()
                }

                override fun onRefreshStationDetail() {
                    viewModel.fetchStationDetail(tag.city, tag.stationUid)
                }

                override fun onNavigationClick(lat: Double, lon: Double) {
                    requireContext().showRouteInGoogleMap(lat = lat, lon = lon)
                }

                override fun onFavoriteAddClick(stationUid: String) {
                    viewModel.addFavorite(stationUid)
                }

                override fun onFavoriteRemoveClick(stationUid: String) {
                    viewModel.removeFavorite(stationUid)
                }
            })
        }

        viewDataBinding.flStationDetail.removeAllViews()
        viewDataBinding.flStationDetail.addView(stationDetailView)
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_up)
        stationDetailView.clearAnimation()
        stationDetailView.startAnimation(animation)

        /* fetch api */
        viewModel.favoriteIsExist(tag.stationUid)
        return false
    }


    override fun onCameraIdle() {
        userIsMoving = false
        googleMap?.let { map ->
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
    }

    override fun onCameraMove() {
        userIsMoving = true
    }

    override fun onMapClick(latLng: LatLng) {
        viewDataBinding.flStationDetail.removeAllViews()
        restoreLastMarker()
    }

    private fun restoreLastMarker() {
        val lastMarkerUid = (lastClickMarker?.tag as? StationVO)?.stationUid
        val target = currentMarkers.firstOrNull { it.id == lastMarkerUid } ?: return
        val tag = target.marker.tag as? StationVO ?: return
        val icon = BitmapDescriptorFactory.fromBitmap(getMarkerBitmap(tag, false))
        target.marker.setIcon(icon)
        lastClickMarker = null
    }

    private fun checkPermissions() {
        if (hasAnyLocationPermissions()) {
            moveToCurrentLocation()
        } else {
            moveCameraToTaiwan()
        }
    }

    private fun getStationDetailView(): StationDetailView? {
        val container = viewDataBinding.flStationDetail
        if (container.childCount <= 0) return null
        return container.getChildAt(container.childCount - 1) as? StationDetailView
    }

    private fun moveToCurrentLocation() {
        try {
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME,
                MIN_DISTANCE,
                locationListener
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
            firebaseAnalyticsUtil.exceptionEvent(e.message ?: SECURITY_ERROR_MESSAGE)
            moveCameraToTaiwan()
        }
    }

    private fun moveCameraToTaiwan() {
        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(TAIWAN_LAT, TAIWAN_LNG),
                CAMERA_ZOOM_LEVEL_CONTAIN_TAIWAN
            )
        )
        onSearchBtnClick(true)
    }

    /* 調整「我當前位置」按鈕位置，設定在右下角 */
    private fun changeMapDefaultUi() {
        val mapView = supportMapFragment?.requireView()
        val locationButton = mapView?.findViewWithTag<View>(GOOGLE_MAP_LOCATION_BUTTON_NAME)
        /* FIXME: 按鈕位置有機率沒更新，先用`post`觀察看看 */
        ((locationButton?.parent) as? ViewGroup)?.post {
            val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.setMargins(0, 0, 30, 30)
            locationButton.layoutParams = layoutParams
        }
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

            val supportCityView = SupportCityView(requireContext()).apply {
                setOnSupportCityListener(object : SupportCityView.OnSupportCityListener {
                    override fun onCityClick(city: City) {
                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                            city.cityCenter,
                            CAMERA_ZOOM_LEVEL
                        )
                        googleMap?.moveCamera(cameraUpdate)
                        firebaseAnalyticsUtil.supportCityClickEvent(city.apiKey)
                    }

                    override fun onGoToSettingClick() {
                        requireContext().gotToAndroidSettings()
                        firebaseAnalyticsUtil.goToSettingClick()
                    }
                })
            }

            val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.expand_search)
            viewDataBinding.flSupportCity.addView(supportCityView)
            supportCityView.startAnimation(anim)
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


    private fun GoogleMap.updateMyLocationEnabled(enable: Boolean) {
        try {
            this.isMyLocationEnabled = enable
            changeMapDefaultUi()
        } catch (e: SecurityException) {
            e.printStackTrace()
            firebaseAnalyticsUtil.exceptionEvent(e.message ?: SECURITY_ERROR_MESSAGE)
        }
    }

    private fun hasAnyLocationPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        return permissions.any { permission ->
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun prepareMarkerBitmaps() {
        ServiceType.entries.forEach { type ->
            listOf(false, true).forEach { light ->
                val iconGenerator = IconGenerator(requireContext()).apply {
                    setBackground(null)
                    setContentView(MarkerView(requireContext()).apply { initView(type, light) })
                }
                markerBitmaps[Pair(type, light)] = iconGenerator.makeIcon()
            }
        }
    }

    private fun getMarkerBitmap(stationVO: StationVO, light: Boolean): Bitmap {
        val type = stationVO.serviceType
        val bitmap = markerBitmaps[Pair(type, light)]
        return if (bitmap != null) {
            bitmap
        } else {
            /* should not happened */
            val iconGenerator = IconGenerator(requireContext()).apply {
                setBackground(null)
                setContentView(MarkerView(requireContext()).apply { initView(type, light) })
            }
            iconGenerator.makeIcon()
        }
    }

    private fun showToast(content: String) {
        val ctx = context ?: return
        ToastUtils.cancel()
        ToastUtils.make()
            .setBgColor(ContextCompat.getColor(ctx, R.color.toast_bg))
            .setTextColor(ContextCompat.getColor(ctx, R.color.white))
            .setGravity(Gravity.BOTTOM, 0, ctx.dpToPx(60))
            .setDurationIsLong(false)
            .show(content)
    }
}