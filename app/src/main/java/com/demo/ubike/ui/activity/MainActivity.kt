package com.demo.ubike.ui.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.library.baseAdapters.BR
import com.demo.ubike.R
import com.demo.ubike.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun layoutId(): Int = R.layout.activity_main

    override val bindingVariable: Int
        get() = BR.mainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 不進入休眠
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}