package com.demo.ubike.ui.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.demo.ubike.R
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.viewmodel.FavoriteViewModel
import com.demo.ubike.data.viewmodel.HomeViewModel
import com.demo.ubike.databinding.FragmentFavoriteBinding
import com.demo.ubike.extension.view.dpToPx
import com.demo.ubike.extension.view.getStatusBarHeight
import com.demo.ubike.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>(),
    OnFavoriteItemClickListener {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    override fun getViewModelClass(): Class<FavoriteViewModel> = FavoriteViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_favorite

    override val bindingVariable: Int = BR.favoriteViewModel

    private val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() })
    private var adapter: FavoriteAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFavorite()
    }

    override fun initObserver() {
        viewModel.favoriteList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter?.submitList(it)
                viewDataBinding.clResultEmpty.visibility = View.GONE
                viewDataBinding.rvList.visibility = View.VISIBLE
            } else {
                viewDataBinding.clResultEmpty.visibility = View.VISIBLE
                viewDataBinding.rvList.visibility = View.GONE
            }
        }

        viewModel.favoriteRemoved.observe(
            viewLifecycleOwner,
            EventObserver {
                if (it) {
                    viewModel.getAllFavorite()
                }
            }
        )

        viewModel.stationDetails.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter?.updateItem(it[0])
            }
        }
    }

    override fun onFavoriteRemoveClick(stationUid: String) {
        showToast(requireContext().getString(R.string.remove_from_favorite))
        viewModel.removeFavorite(stationUid)
    }

    override fun onFavoriteContentClick(entity: FavoriteEntity) {
        homeViewModel.clickFavoriteItem(entity)
    }

    private fun initRecyclerView() {
        adapter = FavoriteAdapter(viewModel)
        adapter?.setOnItemClickListener(this)
        viewDataBinding.rvList.layoutManager = LinearLayoutManager(context)
        viewDataBinding.rvList.adapter = adapter
        viewDataBinding.rvList.setPadding(0, requireActivity().getStatusBarHeight() ?: 120, 0, 40)
    }

    private fun showToast(content: String) {
        ToastUtils.cancel()
        ToastUtils.make()
            .setBgColor(ContextCompat.getColor(requireContext(), R.color.toast_bg))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setGravity(Gravity.BOTTOM, 0, requireContext().dpToPx(60))
            .setDurationIsLong(false)
            .show(content)
    }
}