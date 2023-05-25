package com.demo.ubike.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.demo.ubike.R
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.ServiceType
import com.demo.ubike.databinding.ViewMarkerBinding

@SuppressLint("ViewConstructor")
class MarkerView @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val stationEntity: StationEntity
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewMarkerBinding = ViewMarkerBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        initView()
    }

    private fun initView() {
        val imageView = binding.ivCircle
        val color = ContextCompat.getColor(
            context,
            when (stationEntity.getServiceTypeValue()) {
                ServiceType.UBike1_0 -> R.color.u_bike_1
                ServiceType.UBike2_0 -> R.color.u_bike_2
                ServiceType.TBike -> R.color.t_bike
                ServiceType.PBike -> R.color.p_bike
                ServiceType.KBike -> R.color.k_bike
                else -> R.color.black
            }
        )
        ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color))
    }
}