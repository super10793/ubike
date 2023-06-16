package com.demo.ubike.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.demo.ubike.R
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.ServiceType
import com.demo.ubike.data.model.getServiceTypeByKey
import com.demo.ubike.databinding.ViewMarkerBinding
import com.demo.ubike.extension.view.dpToPx
import com.demo.ubike.extension.view.visible

@SuppressLint("ViewConstructor")
class MarkerView @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val stationEntity: StationEntity,
    private val needHighlight: Boolean
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewMarkerBinding = ViewMarkerBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        initView()
        if (needHighlight) highlight()
    }

    private fun initView() {
        val imageView = binding.ivCircle
        val color = ContextCompat.getColor(
            context,
            when (getServiceTypeByKey(stationEntity.serviceType)) {
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

    private fun highlight() {
        // marker
        val markerLayoutParams = binding.ivMarker.layoutParams
        markerLayoutParams.width = context.dpToPx(50)
        markerLayoutParams.height = context.dpToPx(50)
        binding.ivMarker.layoutParams = markerLayoutParams

        // circle
        val circleLayoutParams = binding.ivCircle.layoutParams
        circleLayoutParams.width = context.dpToPx(28)
        circleLayoutParams.height = context.dpToPx(28)
        binding.ivCircle.translationY = context.dpToPx(7f)
        binding.ivCircle.layoutParams = circleLayoutParams

        // light
        binding.ivLight.visible()
    }
}