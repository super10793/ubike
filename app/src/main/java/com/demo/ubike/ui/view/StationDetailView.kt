package com.demo.ubike.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.demo.ubike.R
import com.demo.ubike.data.model.vo.FavoriteExistVO
import com.demo.ubike.data.model.vo.StationDetailVO
import com.demo.ubike.data.model.vo.StationVO
import com.demo.ubike.databinding.ViewStationDetailBinding
import com.demo.ubike.extension.view.removeFromParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class StationDetailView @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {

        private const val REFRESH_SECONDS = 60
    }

    private val binding: ViewStationDetailBinding = ViewStationDetailBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var timerJob: Job? = null

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var onStationDetailListener: OnStationDetailListener? = null

    private var currentStation: StationVO? = null

    private var currentStationDetail: StationDetailVO? = null

    private var currentFavoriteExistVO: FavoriteExistVO? = null

    init {
        initView()
        initListener()
    }

    private fun startTimer() {
        stopTimer()
        timerJob = coroutineScope.launch {
            while (isActive) {
                onStationDetailListener?.onRefreshStationDetail()
                repeat(REFRESH_SECONDS) {
                    delay(1_000L)
                    binding.tvRefreshTime.text = (REFRESH_SECONDS - it).toString()
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startTimer()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopTimer()
    }

    private fun initView() {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        alpha = 0.95f
        isClickable = true
        isFocusable = true
        background = ContextCompat.getDrawable(context, R.drawable.bg_8_radius_fffcec)
    }

    private fun initListener() {
        binding.btnClose.setOnClickListener {
            onStationDetailListener?.onClose()
            removeFromParent()
        }

        binding.ivNavigation.setOnClickListener {
            val station = currentStation ?: return@setOnClickListener
            onStationDetailListener?.onNavigationClick(
                lat = station.positionLat,
                lon = station.positionLon
            )
        }

        binding.ivFavorite.setOnClickListener {
            val favoriteState = currentFavoriteExistVO ?: return@setOnClickListener
            if (favoriteState.isFavorite) {
                onStationDetailListener?.onFavoriteRemoveClick(favoriteState.stationUid)
            } else {
                onStationDetailListener?.onFavoriteAddClick(favoriteState.stationUid)
            }
        }
    }

    fun setOnStationDetailListener(listener: OnStationDetailListener) {
        onStationDetailListener = listener
    }

    fun renderStation(vo: StationVO) {
        currentStation = vo
        binding.stationVO = vo
    }

    fun renderStationDetail(vo: StationDetailVO) {
        currentStationDetail = vo
        binding.stationDetailVO = vo
    }

    fun renderFavoriteState(vo: FavoriteExistVO) {
        currentFavoriteExistVO = vo
        binding.favoriteExistVO = vo
    }

    interface OnStationDetailListener {

        fun onClose()

        fun onRefreshStationDetail()

        fun onNavigationClick(lat: Double, lon: Double)

        fun onFavoriteAddClick(stationUid: String)

        fun onFavoriteRemoveClick(stationUid: String)
    }
}