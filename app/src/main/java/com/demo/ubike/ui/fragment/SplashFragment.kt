package com.demo.ubike.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.navigation.fragment.findNavController
import com.demo.ubike.R
import com.demo.ubike.data.viewmodel.SplashViewModel
import com.demo.ubike.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>() {
    override fun getViewModelClass(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_splash

    override val bindingVariable: Int
        get() = BR.splashViewModel

    override fun initObserver() {
        viewModel.goHomePage.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchToken()
    }
}