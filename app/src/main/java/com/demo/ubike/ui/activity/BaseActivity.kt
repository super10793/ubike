package com.demo.ubike.ui.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {
    private var _viewDataBinding: VB? = null

    val viewDataBinding
        get() = _viewDataBinding
            ?: throw IllegalStateException("ViewDataBinding accessed before onCreate or after onDestroy.")

    open val enableEdgeToEdge = true

    @LayoutRes
    abstract fun provideLayoutId(): Int

    abstract fun initObserver()

    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (enableEdgeToEdge) enableEdgeToEdge()
        _viewDataBinding = DataBindingUtil.setContentView<VB>(this, provideLayoutId())
        _viewDataBinding?.lifecycleOwner = this

        initView()
        initObserver()
        disableBackPressed()
    }

    override fun onDestroy() {
        _viewDataBinding = null
        super.onDestroy()
    }

    private fun disableBackPressed() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // do nothing
                }
            }
        )
    }
}