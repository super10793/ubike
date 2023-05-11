package com.demo.ubike.ui.main

import androidx.databinding.library.baseAdapters.BR
import com.demo.ubike.R
import com.demo.ubike.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>() {
    override fun getViewModelClass(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_splash

    override val bindingVariable: Int
        get() = BR.splashViewModel

    override fun initObserver() {}
}