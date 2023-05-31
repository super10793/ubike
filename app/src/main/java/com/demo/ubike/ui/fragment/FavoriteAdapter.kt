package com.demo.ubike.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.databinding.ItemFavoriteBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private var itemClickListener: OnFavoriteItemClickListener? = null

    private val differCallback =
        object : DiffUtil.ItemCallback<FavoriteEntity>() {
            override fun areItemsTheSame(
                oldItem: FavoriteEntity,
                newItem: FavoriteEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FavoriteEntity,
                newItem: FavoriteEntity
            ): Boolean {
                val stationUid = (oldItem.stationUID == newItem.stationUID)
                val authorityId = (oldItem.authorityID == newItem.authorityID)
                val stationId = (oldItem.stationID == newItem.stationID)
                return (stationUid && authorityId && stationId)
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

    fun setOnItemClickListener(listener: OnFavoriteItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = asyncListDiffer.currentList[position]
        holder.bind(data)
        holder.binding.ivFavorite.setOnClickListener {
            itemClickListener?.onFavoriteRemoveClick(data.stationUID)
        }

        holder.binding.cardContent.setOnClickListener {
            itemClickListener?.onFavoriteContentClick(data)
        }
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    class ViewHolder private constructor(val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(entity: FavoriteEntity) {
            binding.favoriteEntity = entity
            binding.executePendingBindings()
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
