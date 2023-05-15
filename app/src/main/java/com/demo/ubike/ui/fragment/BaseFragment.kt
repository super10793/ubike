package com.demo.ubike.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.ubike.data.viewmodel.MainViewModel

abstract class BaseFragment<VB : ViewDataBinding, VM : ViewModel> : Fragment() {
    private var _viewDataBinding: VB? = null
    val viewDataBinding get() = _viewDataBinding!!

    val viewModel: VM by lazy { ViewModelProvider(this)[getViewModelClass()] }
    protected val mainViewModel: MainViewModel by activityViewModels()
    abstract fun getViewModelClass(): Class<VM>
    abstract fun layoutId(): Int
    abstract val bindingVariable: Int
    abstract fun initObserver()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewDataBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        _viewDataBinding!!.setVariable(bindingVariable, viewModel)
        _viewDataBinding!!.lifecycleOwner = viewLifecycleOwner
        return _viewDataBinding!!.root
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