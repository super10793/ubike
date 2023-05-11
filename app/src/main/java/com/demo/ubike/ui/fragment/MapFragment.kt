package com.demo.ubike.ui.fragment

import androidx.databinding.library.baseAdapters.BR
import com.demo.ubike.R
import com.demo.ubike.databinding.FragmentMapBinding
import com.demo.ubike.data.viewmodel.MapViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>() {

    companion object {
        fun newInstance() = MapFragment()
    }

    override fun getViewModelClass(): Class<MapViewModel> = MapViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_map

    override val bindingVariable: Int = BR.mapViewModel

    override fun initObserver() {}
}