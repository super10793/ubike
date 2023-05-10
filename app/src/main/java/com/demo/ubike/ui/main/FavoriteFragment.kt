package com.demo.ubike.ui.main

import androidx.databinding.library.baseAdapters.BR
import com.demo.ubike.R
import com.demo.ubike.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>() {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    override fun getViewModelClass(): Class<FavoriteViewModel> = FavoriteViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_favorite

    override val bindingVariable: Int = BR.favoriteViewModel

    override fun initObserver() {}
}