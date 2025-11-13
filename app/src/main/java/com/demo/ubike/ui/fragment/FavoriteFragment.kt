package com.demo.ubike.ui.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.demo.ubike.R
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.vo.FavoriteStationVO
import com.demo.ubike.data.viewmodel.FavoriteViewModel
import com.demo.ubike.data.viewmodel.HomeViewModel
import com.demo.ubike.databinding.DialogErrorBinding
import com.demo.ubike.databinding.FragmentFavoriteBinding
import com.demo.ubike.extension.view.dpToPx
import com.demo.ubike.extension.view.getStatusBarHeight
import com.demo.ubike.extension.view.gone
import com.demo.ubike.extension.view.showRouteInGoogleMap
import com.demo.ubike.extension.view.visible
import com.demo.ubike.result.EventObserver
import com.demo.ubike.utils.FirebaseAnalyticsUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    @Inject
    lateinit var firebaseAnalyticsUtil: FirebaseAnalyticsUtil

    private val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() })
    private val viewModel: FavoriteViewModel by viewModels()
    private var adapter: FavoriteAdapter? = null

    override fun provideLayoutId(): Int = R.layout.fragment_favorite

    override fun initView() {
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteStations()
    }

    override fun initObserver() {
        viewModel.favoriteStations.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter?.submitList(it)
                viewDataBinding.clResultEmpty.gone()
                viewDataBinding.rvList.visible()
            } else {
                viewDataBinding.clResultEmpty.visible()
                viewDataBinding.rvList.gone()
            }
        }

        viewModel.favoriteRemoved.observe(viewLifecycleOwner, EventObserver { removed ->
            if (!removed) return@EventObserver
            viewModel.getFavoriteStations()
        })

        viewModel.error.observe(viewLifecycleOwner, EventObserver {
            val binding = DialogErrorBinding.inflate(layoutInflater)
            val dialog = AlertDialog.Builder(requireContext())
                .setCancelable(true)
                .setView(binding.root)
                .create()

            dialog.window?.apply {
                setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

                /* `setDimAmount()`讓背景半透明，但設置後無效，要先`clearFlags()`再`addFlags()`才有效 */
                clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                setDimAmount(0.5f)
            }

            binding.apply {
                tvMessage.text = it
                tvSubmit.setOnClickListener { dialog.dismiss() }
            }

            dialog.show()
        })
    }

    private fun initRecyclerView() {
        adapter = FavoriteAdapter(object : OnFavoriteItemClickListener {
            override fun onFavoriteContentClick(favoriteStationVO: FavoriteStationVO) {
                homeViewModel.clickFavoriteItem(favoriteStationVO)
            }

            override fun onFavoriteRemoveClick(stationUid: String, stationName: String) {
                showToast(requireContext().getString(R.string.remove_from_favorite))
                viewModel.removeFavorite(stationUid)
                firebaseAnalyticsUtil.favoriteRemoveClickEvent(stationUid, stationName)
            }

            override fun onGoToGoogleMapClick(lat: Double, lon: Double) {
                requireContext().showRouteInGoogleMap(lat = lat, lon = lon)
            }

            override fun onRefresh(stationUid: String, city: City) {
                viewModel.fetchStationDetail(stationUid = stationUid, cityKey = city.apiKey)
            }
        })
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