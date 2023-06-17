package com.demo.ubike.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.GridLayout
import com.demo.ubike.data.model.City
import com.demo.ubike.databinding.ViewSupportCityBinding
import com.demo.ubike.extension.view.visibleIf

@SuppressLint("ViewConstructor")
class SupportCityView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val hasFullLocationPermission: Boolean,
    private val onCityClick: ((city: City) -> Unit),
    private val onGoToSettingClick: (() -> Unit)
) : GridLayout(context, attrs, defStyleAttr) {
    private val binding: ViewSupportCityBinding = ViewSupportCityBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        initListener()
        initView()
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
        binding.btnKinmenCounty.setOnClickListener { onCityClick(City.Kinmen) }
        binding.btnGivePermission.setOnClickListener { onGoToSettingClick() }
    }

    private fun initView() {
        binding.btnGivePermission visibleIf !hasFullLocationPermission
    }
}