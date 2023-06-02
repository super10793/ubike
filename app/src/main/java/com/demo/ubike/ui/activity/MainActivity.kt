package com.demo.ubike.ui.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.demo.ubike.R
import com.demo.ubike.data.model.Error
import com.demo.ubike.databinding.ActivityMainBinding
import com.demo.ubike.ui.dialog.ExceptionDialog
import com.demo.ubike.ui.fragment.SplashFragment
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
        initObserver()
    }

    private fun initObserver() {
        viewModel.exceptionEvent.observe(this) { (errorEnum, errorMsg) ->
            val dialog = ExceptionDialog.builder(this)
                .setOneBtn(true)
                .setContent(errorMsg)
                .setConfirmClickListener {
                    when {
                        getCurrentTopFragment() is SplashFragment -> {
                            return@setConfirmClickListener
                        }

                        (errorEnum == Error.ERR00001) -> {
                            findNavController(R.id.nav_host_fragment).navigate(R.id.splashFragment)
                        }
                    }
                }
                .build()
            dialog.show()
        }
    }

    private fun getCurrentTopFragment(): Fragment? {
        return supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.get(
            0
        )
    }
}