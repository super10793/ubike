package com.demo.ubike.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.databinding.ViewStationDetailBinding
import com.demo.ubike.extension.view.removeFromParent

@SuppressLint("ViewConstructor")
class StationDetailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val station: StationEntity,
    private val closeListener: OnStationDetailCloseListener? = null
) : CardView(context, attrs, defStyleAttr) {
    private val binding: ViewStationDetailBinding = ViewStationDetailBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        initView()
        setListener()
    }

    private fun initView() {
        this.setBackgroundColor(Color.TRANSPARENT)
        binding.stationEntity = station
    }

    private fun setListener() {
        binding.btnClose.setOnClickListener {
            closeListener?.onStationDetailClose()
            this.removeFromParent()
        }
    }

    fun updateStationDetail(stationDetail: StationDetailResponse.Data) {
        binding.stationDetail = stationDetail
    }
}