package com.demo.ubike.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.navigation.fragment.findNavController
import com.demo.ubike.R
import com.demo.ubike.data.model.ApiResult
import com.demo.ubike.data.viewmodel.SplashViewModel
import com.demo.ubike.databinding.FragmentSplashBinding
import com.demo.ubike.ui.dialog.ExceptionDialog
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>() {
    override fun getViewModelClass(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_splash

    override val bindingVariable: Int
        get() = BR.splashViewModel

    override fun initObserver() {
        viewModel.progressComplete.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.homeFragment)
            }
        }

        viewModel.progress.observe(
            viewLifecycleOwner
        ) {
            viewDataBinding.tvProgress.text = getString(R.string.progress, it)
            viewDataBinding.progressbar.progress = it
        }

        viewModel.fetchTokenResult.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is ApiResult.Success<*> -> {
                    viewModel.startProgress()
                }

                is ApiResult.Fail -> {
                    val errorMsg =
                        (it.throwable as? HttpException)?.response()?.errorBody()?.string()
                            ?: it.throwable.message
                    val dialog = ExceptionDialog.builder(requireActivity())
                        .setOneBtn(true)
                        .setContent(
                            requireContext().getString(
                                R.string.api_fail,
                                errorMsg
                            )
                        )
                        .setConfirmClickListener { initView() }
                        .build()
                    dialog.show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewDataBinding.tvProgress.text = getString(R.string.progress, 0)
        viewDataBinding.progressbar.progress = 0
        viewModel.resetProgress()
        viewModel.fetchToken()
    }
}