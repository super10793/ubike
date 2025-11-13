package com.demo.ubike.ui.fragment

import android.Manifest
import android.app.AlertDialog
import android.graphics.Color
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.demo.ubike.R
import com.demo.ubike.data.viewmodel.SplashViewModel
import com.demo.ubike.databinding.DialogErrorBinding
import com.demo.ubike.databinding.FragmentSplashBinding
import com.demo.ubike.result.EventObserver
import com.demo.ubike.utils.FirebaseAnalyticsUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    @Inject
    lateinit var firebaseAnalyticsUtil: FirebaseAnalyticsUtil

    private val viewModel: SplashViewModel by viewModels()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        navigateToHome()
    }

    override fun provideLayoutId(): Int = R.layout.fragment_splash

    override fun initView() {
        viewModel.fetchTokens()
    }

    override fun initObserver() {
        viewModel.fetchTokenSuccess.observe(viewLifecycleOwner, EventObserver {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        })

        viewModel.fetchTokenFail.observe(viewLifecycleOwner, EventObserver {
            val ctx = context ?: return@EventObserver
            val binding = DialogErrorBinding.inflate(layoutInflater)
            val dialog = AlertDialog.Builder(ctx)
                .setCancelable(false)
                .setView(binding.root)
                .create()

            dialog.window?.apply {
                setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

                /* `setDimAmount()`讓背景半透明，但設置後無效，要先`clearFlags()`再`addFlags()`才有效 */
                clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                setDimAmount(0.5f)
            }

            binding.apply {
                tvMessage.text = getString(R.string.api_fail, it.message.orEmpty())
                tvSubmit.setOnClickListener {
                    viewModel.fetchTokens()
                    dialog.dismiss()
                }
            }

            dialog.show()
        })
    }

    private fun navigateToHome() {
        firebaseAnalyticsUtil.loginEvent()
        findNavController().navigate(R.id.homeFragment)
    }
}