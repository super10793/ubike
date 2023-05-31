package com.demo.ubike.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.demo.ubike.BR
import com.demo.ubike.R
import com.demo.ubike.data.viewmodel.HomeViewModel
import com.demo.ubike.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BasePermissionFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_home

    override val bindingVariable: Int = BR.homeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initNavView()
    }

    override fun initObserver() {
        viewModel.favoriteItemClicked.observe(viewLifecycleOwner) {
            viewDataBinding.viewPager.currentItem = 0
        }
    }

    private fun initViewPager() {
        val fragments = listOf(
            MapFragment.newInstance(),
            FavoriteFragment.newInstance()
        )

        viewDataBinding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }

        viewDataBinding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewDataBinding.navView.selectedItemId = when (position) {
                    0 -> R.id.mapFragment
                    1 -> R.id.favoriteFragment
                    else -> R.id.mapFragment
                }
            }
        })

        viewDataBinding.viewPager.isUserInputEnabled = false
    }

    private fun initNavView() {
        viewDataBinding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mapFragment -> {
                    viewDataBinding.viewPager.currentItem = 0
                    true
                }

                R.id.favoriteFragment -> {
                    viewDataBinding.viewPager.currentItem = 1
                    true
                }

                else -> false
            }
        }
    }
}