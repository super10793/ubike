package com.demo.ubike.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.ubike.R
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.viewmodel.FavoriteViewModel
import com.demo.ubike.data.viewmodel.HomeViewModel
import com.demo.ubike.databinding.FragmentFavoriteBinding
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
    }
}