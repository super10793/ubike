package com.demo.ubike.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {
    private var _viewDataBinding: VB? = null
    val viewDataBinding
        get() = requireNotNull(_viewDataBinding) {
            "ViewDataBinding accessed before onCreate or after onDestroy."
        }

    abstract val layoutId: Int

    abstract fun initObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewDataBinding = DataBindingUtil.setContentView<VB>(this, layoutId).apply {
            lifecycleOwner = this@BaseActivity
        }
        initObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewDataBinding = null
    }

    override fun onBackPressed() {
        //do nothing
    }
}