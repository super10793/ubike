package com.demo.ubike.ui.activity

import android.view.WindowManager
import androidx.activity.viewModels
import com.demo.ubike.R
import com.demo.ubike.data.viewmodel.MainViewModel
import com.demo.ubike.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    override fun provideLayoutId(): Int = R.layout.activity_main

    override fun initObserver() {
        // nothing
    }

    override fun initView() {
        // 不進入休眠
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}