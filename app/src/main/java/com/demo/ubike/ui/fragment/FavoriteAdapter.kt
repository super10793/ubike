package com.demo.ubike.ui.fragment

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.demo.ubike.data.local.favorite.CustomPayload
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.data.viewmodel.FavoriteViewModel
import com.demo.ubike.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private val favoriteViewModel: FavoriteViewModel
) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private var itemClickListener: OnFavoriteItemClickListener? = null
    private val visibleItems = mutableSetOf<String>()

    private val differCallback =
        object : DiffUtil.ItemCallback<FavoriteEntity>() {
            override fun areItemsTheSame(
                oldItem: FavoriteEntity,
                newItem: FavoriteEntity
            ): Boolean {
                return oldItem.stationUID == newItem.stationUID
            }

            override fun areContentsTheSame(
                oldItem: FavoriteEntity,
                newItem: FavoriteEntity
            ): Boolean {
                val stationUid = (oldItem.stationUID == newItem.stationUID)
                val authorityId = (oldItem.authorityID == newItem.authorityID)
                val stationId = (oldItem.stationID == newItem.stationID)
                val city = (oldItem.city == newItem.city)
                val bikesCapacity = (oldItem.bikesCapacity == newItem.bikesCapacity)
                val serviceType = (oldItem.serviceType == newItem.serviceType)
                val positionLon = (oldItem.positionLon == newItem.positionLon)
                val positionLat = (oldItem.positionLat == newItem.positionLat)
                val stationNameZhTw = (oldItem.stationNameZhTw == newItem.stationNameZhTw)
                val stationNameEn = (oldItem.stationNameEn == newItem.stationNameEn)
                val stationAddressZhTw = (oldItem.stationAddressZhTw == newItem.stationAddressZhTw)
                val stationAddressEn = (oldItem.stationAddressEn == newItem.stationAddressEn)

                return (stationUid && authorityId &&
                        stationId && city &&
                        bikesCapacity && serviceType &&
                        positionLon && positionLat &&
                        stationNameZhTw && stationNameEn &&
                        stationAddressZhTw && stationAddressEn)
            }
        }

    private val asyncListDiffer = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<FavoriteEntity>) {
        asyncListDiffer.submitList(
            list.map {
                it.copy()
            }
        )
    }

    fun updateItem(item: StationDetailResponse.Data) {
        val currentList = asyncListDiffer.currentList.toMutableList()
        val index = currentList.indexOfFirst { it.stationUID == item.stationUID }
        if (index != -1) {
            notifyItemChanged(
                index, CustomPayload(
                    stationUid = item.stationUID,
                    serviceStatus = item.serviceStatus,
                    availableRentBikes = item.availableRentBikes,
                    availableReturnBikes = item.availableReturnBikes,
                    availableRentGeneralBikes = item.availableRentBikesDetail.generalBikes,
                    availableRentElectricBikes = item.availableRentBikesDetail.electricBikes,
                    updateTime = item.updateTime
                )
            )
        }
    }

    fun setOnItemClickListener(listener: OnFavoriteItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = asyncListDiffer.currentList[position]
        holder.bind(data, favoriteViewModel)
        holder.binding.ivFavorite.setOnClickListener {
            itemClickListener?.onFavoriteRemoveClick(data.stationUID, data.stationNameZhTw)
        }

        holder.binding.cardContent.setOnClickListener {
            itemClickListener?.onFavoriteContentClick(data)
        }

        holder.binding.ivNavigation.setOnClickListener {
            itemClickListener?.onGoToGoogleMapClick(data.positionLat, data.positionLon)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val payload = payloads[0] as? CustomPayload
            payload?.let {
                holder.bindPayloads(it)
            }
        }
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val city = holder.binding.favoriteEntity?.city
        val stationUid = holder.binding.favoriteEntity?.stationUID
        if (city != null && stationUid != null) {
            favoriteViewModel.fetchStationDetail(city, stationUid)
            visibleItems.add(stationUid)
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val stationUid = holder.binding.favoriteEntity?.stationUID
        stationUid?.let {
            visibleItems.remove(it)
        }
        holder.resetPayloads()
        holder.stopTimer()
    }

    fun getVisibleItems(): MutableSet<String> = visibleItems

    class ViewHolder private constructor(val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val countDownMillis = 60000L
        private val countDownInterval = 1000L
        private var countDownTimer: CountDownTimer? = null
        private lateinit var viewModel: FavoriteViewModel

        fun bind(entity: FavoriteEntity, favoriteViewModel: FavoriteViewModel) {
            viewModel = favoriteViewModel
            binding.favoriteEntity = entity
            resetPayloads()
            binding.executePendingBindings()
        }

        fun bindPayloads(payload: CustomPayload) {
            binding.payload = payload
            startTimer()
        }

        fun resetPayloads() {
            binding.payload = CustomPayload()
        }

        private fun startTimer() {
            stopTimer()
            countDownTimer = object : CountDownTimer(countDownMillis, countDownInterval) {
                @SuppressLint("SetTextI18n")
                override fun onFinish() {
                    binding.favoriteEntity?.let {
                        viewModel.fetchStationDetail(it.city, it.stationUID)
                    }
                }

                override fun onTick(millisUntilFinished: Long) {
                    // 倒數中
                }
            }.start()
        }

        fun stopTimer() {
            countDownTimer?.cancel()
            countDownTimer = null
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemFavoriteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
