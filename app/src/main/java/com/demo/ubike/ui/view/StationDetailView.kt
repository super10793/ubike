package com.demo.ubike.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.blankj.utilcode.util.ToastUtils
import com.demo.ubike.R
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.data.viewmodel.MapViewModel
import com.demo.ubike.databinding.ViewStationDetailBinding
import com.demo.ubike.extension.view.dpToPx
import com.demo.ubike.extension.view.removeFromParent
import com.demo.ubike.utils.EventObserver
import com.demo.ubike.utils.FirebaseAnalyticsUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("ViewConstructor")
@AndroidEntryPoint
class StationDetailView @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val station: StationEntity,
    private val listener: OnStationDetailListener? = null
) : CardView(context, attrs, defStyleAttr) {
    private val binding: ViewStationDetailBinding = ViewStationDetailBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    @Inject
    lateinit var firebaseAnalyticsUtil: FirebaseAnalyticsUtil

    private val viewModel by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!)[MapViewModel::class.java]
    }

    private val stationDetailObserver = EventObserver<StationDetailResponse> {
        binding.stationDetail = it[0]
    }

    private val isFavoriteObserver =
        EventObserver<Pair<String, Boolean>> { (stationUid, isFavorite) ->
            binding.isFavorite = isFavorite
        }

    init {
        initView()
        setListener()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initObserver()
        viewModel.favoriteIsExist(station.stationUID)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewModel.stationDetail.removeObserver(stationDetailObserver)
        viewModel.isFavorite.removeObserver(isFavoriteObserver)
    }

    private fun initView() {
        this.setBackgroundColor(Color.TRANSPARENT)
        binding.stationEntity = station

    }

    private fun setListener() {
        binding.btnClose.setOnClickListener {
            listener?.onStationDetailClose()
            removeFromParent()
        }

        binding.ivNavigation.setOnClickListener {
            listener?.onGoToGoogleMapClick(
                station.positionLat,
                station.positionLon
            )
        }

        binding.ivFavorite.setOnClickListener {
            when (binding.isFavorite) {
                true -> {
                    viewModel.removeFavorite(station.stationUID)
                    showToast(context.getString(R.string.remove_from_favorite))
                    firebaseAnalyticsUtil.favoriteRemoveClickEvent(
                        station.stationUID,
                        station.stationNameZhTw
                    )
                }

                else -> {
                    val entity = FavoriteEntity(
                        stationUID = station.stationUID,
                        stationID = station.stationID,
                        authorityID = station.authorityID,
                        city = station.city,
                        bikesCapacity = station.bikesCapacity,
                        serviceType = station.serviceType,
                        positionLon = station.positionLon,
                        positionLat = station.positionLat,
                        stationNameZhTw = station.stationNameZhTw,
                        stationNameEn = station.stationNameEn,
                        stationAddressZhTw = station.stationAddressZhTw,
                        stationAddressEn = station.stationAddressEn,
                    )
                    viewModel.addFavorite(entity)
                    showToast(context.getString(R.string.added_to_favorite))
                    firebaseAnalyticsUtil.favoriteAddClickEvent(
                        station.stationUID,
                        station.stationNameZhTw
                    )
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.isFavorite.observe(
            findViewTreeLifecycleOwner()!!,
            isFavoriteObserver
        )

        viewModel.stationDetail.observe(
            findViewTreeLifecycleOwner()!!,
            stationDetailObserver
        )
    }

    private fun showToast(content: String) {
        ToastUtils.cancel()
        ToastUtils.make()
            .setBgColor(ContextCompat.getColor(context, R.color.toast_bg))
            .setTextColor(ContextCompat.getColor(context, R.color.white))
            .setGravity(Gravity.BOTTOM, 0, context.dpToPx(60))
            .setDurationIsLong(false)
            .show(content)
    }
}