package com.demo.ubike.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment<VB : ViewDataBinding> : DialogFragment() {
    private var _viewDataBinding: VB? = null
    val viewDataBinding
        get() = _viewDataBinding
            ?: throw IllegalStateException("ViewDataBinding accessed before onCreateView or after onDestroyView.")

    @LayoutRes
    abstract fun provideLayoutId(): Int

    abstract fun initView()

    abstract fun initObserver()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewDataBinding = DataBindingUtil.inflate<VB>(
            inflater,
            provideLayoutId(),
            container,
            false
        )

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.lifecycleOwner = viewLifecycleOwner

        initView()
        initObserver()
    }

    override fun onDestroyView() {
        _viewDataBinding = null
        super.onDestroyView()
    }
}