package com.demo.ubike.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.GridLayout
import com.demo.ubike.data.model.City
import com.demo.ubike.databinding.ViewSupportCityBinding

@SuppressLint("ViewConstructor")
class SupportCityView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val onCityClick: ((city: City) -> Unit)
) : GridLayout(context, attrs, defStyleAttr) {
    private val binding: ViewSupportCityBinding = ViewSupportCityBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        initListener()
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
    }
}