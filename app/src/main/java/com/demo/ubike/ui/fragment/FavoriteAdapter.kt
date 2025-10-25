package com.demo.ubike.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.vo.FavoriteStationVO
import com.demo.ubike.databinding.ItemFavoriteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class FavoriteAdapter(private val itemClickListener: OnFavoriteItemClickListener) :
    ListAdapter<FavoriteStationVO, FavoriteAdapter.FavoriteViewHolder>(FavoriteStationVO.Differ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val holder = FavoriteViewHolder.from(parent).apply {
            binding.ivFavorite.setOnClickListener {
                val pos = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return@setOnClickListener
                val data = getItem(pos)
                itemClickListener.onFavoriteRemoveClick(data.stationUid, data.stationNameZhTw)
            }

            binding.cardContent.setOnClickListener {
                val pos = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return@setOnClickListener
                val data = getItem(pos)
                itemClickListener.onFavoriteContentClick(data)
            }

            binding.ivNavigation.setOnClickListener {
                val pos = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return@setOnClickListener
                val data = getItem(pos)
                itemClickListener.onGoToGoogleMapClick(data.positionLat, data.positionLon)
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewAttachedToWindow(holder: FavoriteViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.startCountdown { stationUid, city -> itemClickListener.onRefresh(stationUid, city) }
    }

    override fun onViewDetachedFromWindow(holder: FavoriteViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.stopCountdown()
    }

    class FavoriteViewHolder private constructor(val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            private const val REFRESH_SECONDS = 60

            fun from(parent: ViewGroup): FavoriteViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemFavoriteBinding.inflate(layoutInflater, parent, false)
                return FavoriteViewHolder(binding)
            }
        }

        private var countdownJob: Job? = null
        private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

        fun bind(vo: FavoriteStationVO) {
            binding.favoriteStationVO = vo
        }

        fun startCountdown(onTimeUp: (stationUid: String, city: City) -> Unit) {
            if (countdownJob?.isActive == true) return

            countdownJob = coroutineScope.launch {
                while (isActive) {
                    binding.favoriteStationVO?.let { onTimeUp(it.stationUid, it.city) }
                    repeat(REFRESH_SECONDS) {
                        delay(1_000L)
                        binding.tvRefreshTime.text = (REFRESH_SECONDS - it).toString()
                    }
                }
            }
        }

        fun stopCountdown() {
            countdownJob?.cancel()
            countdownJob = null
        }
    }
}
