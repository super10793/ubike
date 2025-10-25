package com.demo.ubike.ui.bindingadapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.demo.ubike.R
import com.demo.ubike.data.model.ServiceStatus
import com.demo.ubike.data.model.ServiceType
import com.demo.ubike.data.model.vo.FavoriteExistVO
import com.demo.ubike.data.model.vo.StationDetailVO
import com.demo.ubike.extension.view.invisible
import com.demo.ubike.extension.view.visible
import java.text.SimpleDateFormat
import java.util.Locale

@BindingAdapter("stationStatusText")
fun setStationStatusText(
    textview: AppCompatTextView,
    serviceStatus: ServiceStatus?
) {
    when (serviceStatus) {
        null -> {
            textview.invisible()
        }

        else -> {
            val context = textview.context
            val text = when (serviceStatus) {
                ServiceStatus.Stop -> context.getString(R.string.status_stop)
                ServiceStatus.Normal -> context.getString(R.string.status_normal)
                ServiceStatus.Pause -> context.getString(R.string.status_pause)
                else -> context.getString(R.string.status_normal)
            }
            val color = when (serviceStatus) {
                ServiceStatus.Stop -> R.color.status_stop
                ServiceStatus.Normal -> R.color.status_normal
                ServiceStatus.Pause -> R.color.status_pause
                else -> R.color.status_normal
            }

            textview.visible()
            textview.text = text
            textview.setTextColor(ContextCompat.getColor(context, color))
        }
    }
}

@BindingAdapter("stationBikesText")
fun setStationBikesText(
    textview: AppCompatTextView,
    vo: StationDetailVO?
) {
    vo?.let {
        val context = textview.context
        val rent = it.canRentBikes.toString()
        val text = context.getString(R.string.available_rent, rent)
        textview.text = text
    }
}

@BindingAdapter("stationReturnBikeText")
fun setStationReturnBikeText(
    textview: AppCompatTextView,
    vo: StationDetailVO?
) {
    vo?.let {
        val context = textview.context
        val bikeReturn = it.canReturnBikes.toString()
        textview.text = context.getString(R.string.available_return, bikeReturn)
    }
}

@BindingAdapter("statusLight")
fun setStatusLight(
    imageView: AppCompatImageView,
    serviceStatus: ServiceStatus?
) {
    when (serviceStatus) {
        null -> {
            imageView.invisible()
        }

        else -> {
            imageView.visible()
            val imageResource = when (serviceStatus) {
                ServiceStatus.Normal -> R.drawable.status_normal_circle
                ServiceStatus.Pause -> R.drawable.status_pause_circle
                ServiceStatus.Stop -> R.drawable.status_stop_circle
                else -> R.drawable.status_stop_circle
            }
            imageView.setImageResource(imageResource)
        }
    }
}

@BindingAdapter("setFavoriteState")
fun setFavoriteState(
    imageView: AppCompatImageView,
    vo: FavoriteExistVO?
) {
    val imageResource = when (vo?.isFavorite) {
        true -> R.drawable.favorite
        else -> R.drawable.favorite_border
    }
    imageView.setImageResource(imageResource)
}

@BindingAdapter("showBlockImage")
fun setShowBlockImage(
    imageView: AppCompatImageView,
    vo: StationDetailVO?
) {
    vo?.let {
        imageView.visibility = when (it.serviceStatus) {
            ServiceStatus.Pause -> View.VISIBLE
            ServiceStatus.Stop -> View.VISIBLE
            else -> View.GONE
        }
    }
}

@BindingAdapter("formatStationName")
fun setFormatStationName(
    textview: AppCompatTextView,
    stationName: String?
) {
    textview.text = stationName
        ?.replace("YouBike1.0_", "", true)
        ?.replace("YouBike2.0_", "", true)
        ?.replace("iBike1.0_", "", true)
        ?: ""
}

@BindingAdapter("stationIconColor")
fun setStationIconColor(
    imageview: AppCompatImageView,
    serviceType: ServiceType?
) {
    serviceType?.let {
        val context = imageview.context
        val color = when (it) {
            ServiceType.UBike1_0 -> R.color.u_bike_1
            ServiceType.UBike2_0 -> R.color.u_bike_2
            ServiceType.TBike -> R.color.t_bike
            ServiceType.PBike -> R.color.p_bike
            ServiceType.KBike -> R.color.k_bike
            else -> R.color.u_bike_2
        }
        val tint = ContextCompat.getColor(context, color)
        imageview.setColorFilter(tint)
    }
}

@BindingAdapter("stationTxtAndColor")
fun setStationTxtAndColor(
    textview: AppCompatTextView,
    stationType: ServiceType?
) {
    stationType?.let {
        val context = textview.context
        val color = when (it) {
            ServiceType.UBike1_0 -> R.color.u_bike_1
            ServiceType.UBike2_0 -> R.color.u_bike_2
            ServiceType.TBike -> R.color.t_bike
            ServiceType.PBike -> R.color.p_bike
            ServiceType.KBike -> R.color.k_bike
            else -> R.color.u_bike_2
        }
        val txt = when (it) {
            ServiceType.UBike1_0 -> context.getString(R.string.ubike_1)
            ServiceType.UBike2_0 -> context.getString(R.string.ubike_2)
            ServiceType.TBike -> context.getString(R.string.tbike)
            ServiceType.PBike -> context.getString(R.string.pbike)
            ServiceType.KBike -> context.getString(R.string.kbike)
            else -> context.getString(R.string.ubike_2)
        }
        textview.setTextColor(ContextCompat.getColor(context, color))
        textview.text = context.getString(R.string.symbol_colon, txt)
    }
}

@BindingAdapter("formatUpdateTime")
fun setFormatUpdateTime(
    textview: AppCompatTextView,
    updateTime: String?
) {
    if (updateTime.isNullOrBlank()) {
        textview.invisible()
    } else {
        val context = textview.context
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
        val formatDate = format.parse(updateTime)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val result = formatDate?.let {
            outputFormat.format(it)
        } ?: ""
        textview.visible()
        textview.text = context.getString(R.string.data_update_time, result)
    }
}

@BindingAdapter("formatNullableInt")
fun setFormatNullableInt(
    textview: AppCompatTextView,
    content: Int?
) {
    textview.text = content?.toString() ?: ""
}

@BindingAdapter("stationStatusFavoriteText")
fun setStationStatusFavoriteText(
    textview: AppCompatTextView,
    status: ServiceStatus?
) {
    when (status) {
        null -> {
            textview.invisible()
        }

        else -> {
            val context = textview.context
            textview.visible()
            textview.text = when (status) {
                ServiceStatus.Stop -> context.getString(R.string.status_stop_with_newline)
                ServiceStatus.Normal -> context.getString(R.string.status_normal_with_newline)
                ServiceStatus.Pause -> context.getString(R.string.status_pause_with_newline)
                else -> context.getString(R.string.status_normal_with_newline)
            }
        }
    }
}

@BindingAdapter("stationStatusFavoriteIcon")
fun setStationStatusFavoriteIcon(
    imageView: AppCompatImageView,
    status: ServiceStatus?
) {
    when (status) {
        null -> {
            imageView.invisible()
        }

        else -> {
            imageView.visible()
            val imageResource = when (status) {
                ServiceStatus.Normal -> R.drawable.status_ok
                ServiceStatus.Pause -> R.drawable.status_warn
                ServiceStatus.Stop -> R.drawable.status_stop
                else -> R.drawable.status_ok
            }
            imageView.setImageResource(imageResource)
        }
    }
}

@BindingAdapter("favoriteStationReturnBikeText")
fun setFavoriteStationReturnBikeText(
    textview: AppCompatTextView,
    returnBike: Int?
) {
    val context = textview.context
    textview.text = context.getString(R.string.available_return, returnBike?.toString() ?: "")
}

@BindingAdapter(
    value = ["serviceType", "normalBikesCount"],
    requireAll = true
)
fun setNormalBikesCount(
    textview: AppCompatTextView,
    serviceType: ServiceType?,
    normalBikesCount: Int?
) {
    if (serviceType != null && normalBikesCount != null) {
        val context = textview.context
        val color = when (serviceType) {
            ServiceType.UBike1_0 -> R.color.u_bike_1
            ServiceType.UBike2_0 -> R.color.u_bike_2
            ServiceType.TBike -> R.color.t_bike
            ServiceType.PBike -> R.color.p_bike
            ServiceType.KBike -> R.color.k_bike
            else -> R.color.u_bike_2
        }
        textview.setTextColor(ContextCompat.getColor(context, color))
        textview.text = "$normalBikesCount"
    }
}

@BindingAdapter("electricBikesCount")
fun setElectricBikesCount(
    textview: AppCompatTextView,
    electricCount: Int?
) {
    electricCount?.let { textview.text = "$electricCount" }
}

@BindingAdapter("invisibleIfContentInvalid")
fun setInvisibleIfContentInvalid(
    textview: AppCompatTextView,
    content: String?
) {
    textview.visibility = if (content.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
}