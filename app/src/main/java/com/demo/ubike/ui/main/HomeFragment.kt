package com.demo.ubike.ui.main

import android.os.Bundle
import android.view.View
import com.demo.ubike.BR
import com.demo.ubike.R
import com.demo.ubike.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_home

    override val bindingVariable: Int = BR.homeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchData()
    }

    override fun initObserver() {
        viewModel.apiResult.observe(viewLifecycleOwner) {
            viewDataBinding.tvHint.text = it.title
        }
    }
}