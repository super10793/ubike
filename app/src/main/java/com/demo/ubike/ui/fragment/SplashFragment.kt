package com.demo.ubike.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.navigation.fragment.findNavController
import com.demo.ubike.R
import com.demo.ubike.data.viewmodel.SplashViewModel
import com.demo.ubike.databinding.FragmentSplashBinding
import com.demo.ubike.result.EventObserver
import com.demo.ubike.ui.dialog.ExceptionDialog
import com.demo.ubike.utils.FirebaseAnalyticsUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>() {

    @Inject
    lateinit var firebaseAnalyticsUtil: FirebaseAnalyticsUtil
    override fun getViewModelClass(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_splash

    override val bindingVariable: Int
        get() = BR.splashViewModel

    override fun initObserver() {
        viewModel.fetchTokenSuccess.observe(viewLifecycleOwner, EventObserver {
            firebaseAnalyticsUtil.loginEvent()
            findNavController().navigate(R.id.homeFragment)
        })

        viewModel.fetchTokenFail.observe(viewLifecycleOwner, EventObserver {
            val content = getString(R.string.api_fail, it.message.orEmpty())
            ExceptionDialog.builder(requireActivity())
                .setOneBtn(true)
                .setContent(content)
                .setConfirmClickListener { viewModel.fetchTokens() }
                .build()
                .show()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchTokens()
    }
}