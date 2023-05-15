package com.demo.ubike.ui.activity

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import com.demo.ubike.R
import com.demo.ubike.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun layoutId(): Int = R.layout.activity_main

    override val bindingVariable: Int
        get() = BR.mainViewModel
}