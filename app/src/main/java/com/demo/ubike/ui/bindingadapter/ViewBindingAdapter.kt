package com.demo.ubike.ui.bindingadapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.demo.ubike.R
import com.demo.ubike.data.model.ServiceStatus
import com.demo.ubike.data.model.StationDetailResponse

@BindingAdapter("stationStatusText")
fun setStationStatusText(
    textview: AppCompatTextView,
    stationDetail: StationDetailResponse.Data?
) {
    stationDetail?.let {
        val context = textview.context
        val text = when (it.getServiceStatus()) {
            ServiceStatus.Stop -> context.getString(R.string.status_stop)
            ServiceStatus.Normal -> context.getString(R.string.status_normal)
            ServiceStatus.Pause -> context.getString(R.string.status_pause)
            else -> context.getString(R.string.status_normal)
        }
        val color = when (it.getServiceStatus()) {
            ServiceStatus.Stop -> R.color.status_stop
            ServiceStatus.Normal -> R.color.status_normal
            ServiceStatus.Pause -> R.color.status_pause
            else -> R.color.status_normal
        }

        textview.text = text
        textview.setTextColor(ContextCompat.getColor(context, color))
    }
}

@BindingAdapter("stationBikesText")
fun setStationBikesText(
    textview: AppCompatTextView,
    stationDetail: StationDetailResponse.Data?
) {
    stationDetail?.let {
        val context = textview.context
        val rent = it.availableRentBikes.toString()
        val text = context.getString(R.string.available_rent, rent)
        textview.text = text
    }
}

@BindingAdapter("stationBikesDetailText")
fun setStationBikesDetailText(
    textview: AppCompatTextView,
    stationDetail: StationDetailResponse.Data?
) {
    stationDetail?.let {
        val context = textview.context
        val generalBikes = it.availableRentBikesDetail.generalBikes.toString()
        val electricBikes = it.availableRentBikesDetail.electricBikes.toString()
        val text = context.getString(R.string.available_rent_detail, electricBikes, generalBikes)
        textview.text = text
    }
}

@BindingAdapter("stationReturnBikeText")
fun setStationReturnBikeText(
    textview: AppCompatTextView,
    stationDetail: StationDetailResponse.Data?
) {
    stationDetail?.let {
        val context = textview.context
        val bikeReturn = it.availableReturnBikes.toString()
        textview.text = context.getString(R.string.available_return, bikeReturn)
    }
}

@BindingAdapter("stationUpdateTimeText")
fun setStationUpdateTimeText(
    textview: AppCompatTextView,
    stationDetail: StationDetailResponse.Data?
) {
    stationDetail?.let {
        val context = textview.context
        val time = it.updateTime
        textview.text = context.getString(R.string.data_update_time, time)
    }
}

@BindingAdapter("statusLight")
fun setStatusLight(
    imageView: AppCompatImageView,
    stationDetail: StationDetailResponse.Data?
) {
    stationDetail?.let {
        val imageResource = when (it.getServiceStatus()) {
            ServiceStatus.Normal -> R.drawable.status_normal_circle
            ServiceStatus.Pause -> R.drawable.status_pause_circle
            ServiceStatus.Stop -> R.drawable.status_stop_circle
            else -> R.drawable.status_stop_circle
        }
        imageView.setImageResource(imageResource)
    }
}

@BindingAdapter("isFavorite")
fun setIsFavorite(
    imageView: AppCompatImageView,
    isFavorite: Boolean?
) {
    val imageResource = when (isFavorite) {
        true -> R.drawable.favorite
        else -> R.drawable.favorite_border
    }
    imageView.setImageResource(imageResource)
}

@BindingAdapter("showBlockImage")
fun setShowBlockImage(
    imageView: AppCompatImageView,
    stationDetail: StationDetailResponse.Data?
) {
    stationDetail?.let {
        imageView.visibility = when (it.getServiceStatus()) {
            ServiceStatus.Pause -> View.VISIBLE
            ServiceStatus.Stop -> View.VISIBLE
            else -> View.GONE
        }
    }
}
