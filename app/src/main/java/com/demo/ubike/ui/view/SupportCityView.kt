package com.demo.ubike.ui.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.demo.ubike.data.model.City
import com.demo.ubike.databinding.ViewSupportCityBinding
import com.demo.ubike.extension.view.visibleIf

class SupportCityView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding: ViewSupportCityBinding = ViewSupportCityBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var onSupportCityListener: OnSupportCityListener? = null

    init {
        initView()
        initListener()
    }

    fun setOnSupportCityListener(listener: OnSupportCityListener) {
        this.onSupportCityListener = listener
    }

    private fun initListener() {
        binding.btnTaipei.setOnClickListener { onCityClick(City.Taipei) }
        binding.btnNewTaipei.setOnClickListener { onCityClick(City.NewTaipei) }
        binding.btnTaoyuan.setOnClickListener { onCityClick(City.Taoyuan) }
        binding.btnHsinchu.setOnClickListener { onCityClick(City.Hsinchu) }
        binding.btnHsinchuCounty.setOnClickListener { onCityClick(City.HsinchuCounty) }
        binding.btnMiaoliCounty.setOnClickListener { onCityClick(City.Miaoli) }
        binding.btnTaichung.setOnClickListener { onCityClick(City.Taichung) }
        binding.btnChiayi.setOnClickListener { onCityClick(City.Chiayi) }
        binding.btnKaohsiung.setOnClickListener { onCityClick(City.Kaohsiung) }
        binding.btnTainan.setOnClickListener { onCityClick(City.Tainan) }
        binding.btnPingtungCounty.setOnClickListener { onCityClick(City.Pingtung) }
        binding.btnChanghua.setOnClickListener { onCityClick(City.Changhua) }
        binding.btnYunlin.setOnClickListener { onCityClick(City.Yunlin) }
        binding.btnTaitung.setOnClickListener { onCityClick(City.Taitung) }
        binding.btnChiayiCounty.setOnClickListener { onCityClick(City.ChiayiCounty) }

        binding.btnGivePermission.setOnClickListener { onSupportCityListener?.onGoToSettingClick() }
    }

    private fun onCityClick(city: City) {
        onSupportCityListener?.onCityClick(city)
    }

    private fun initView() {
        orientation = VERTICAL
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        binding.btnGivePermission visibleIf !hasFullLocationPermissions()
    }

    private fun hasFullLocationPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    interface OnSupportCityListener {

        fun onCityClick(city: City)

        fun onGoToSettingClick()
    }
}