package com.demo.ubike.ui.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.demo.ubike.R
import com.demo.ubike.data.viewmodel.MainViewModel
import com.demo.ubike.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    override val layoutId: Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initObserver() {
        // nothing
    }

    private fun initView() {
        // 不進入休眠
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // statusBar、navigationBar 透明
        val rootView = viewDataBinding.root
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        WindowCompat.getInsetsController(window, rootView).isAppearanceLightStatusBars = true
        WindowCompat.getInsetsController(window, rootView).isAppearanceLightNavigationBars = true
    }
}