package com.demo.ubike.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment<VB : ViewDataBinding> : DialogFragment() {
    private var _viewDataBinding: VB? = null
    val viewDataBinding
        get() = requireNotNull(_viewDataBinding) {
            "ViewDataBinding accessed before onCreateView or after onDestroyView."
        }

    abstract val layoutId: Int

    abstract fun initObserver()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewDataBinding = DataBindingUtil.inflate<VB>(
            inflater,
            layoutId,
            container,
            false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewDataBinding = null
    }
}