package com.demo.ubike.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.demo.ubike.data.viewmodel.MainViewModel

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    private var _viewDataBinding: T? = null
    val viewDataBinding get() = _viewDataBinding!!


    protected val viewModel: MainViewModel by
    lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    abstract fun layoutId(): Int

    abstract val bindingVariable: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewDataBinding = DataBindingUtil.setContentView(this, layoutId())
        _viewDataBinding!!.setVariable(bindingVariable, viewModel)
        _viewDataBinding!!.lifecycleOwner = this
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewDataBinding = null
    }

    override fun onBackPressed() {
        //do nothing
    }
}