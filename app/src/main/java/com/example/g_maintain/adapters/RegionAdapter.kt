package com.example.g_maintain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.g_maintain.databinding.RvClientRegionBinding
import com.example.g_maintain.db.Region

class RegionAdapter(private val regionClicked: RegionClicked) : PagingDataAdapter<Region, RegionAdapter.RegionViewHolder>(MyCallback()) {
    inner class RegionViewHolder(private val binding: RvClientRegionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val region = getItem(position)
                    if (region != null) {
                        regionClicked.onRegionClicked(region)
                    }
                }
            }
        }
        fun bind(region: Region) {
            binding.txtRvClientRegion.text = region.regionName
        }
    }

    class MyCallback : DiffUtil.ItemCallback<Region>() {
        override fun areItemsTheSame(oldItem: Region, newItem: Region) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Region, newItem: Region) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val binding =
            RvClientRegionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RegionViewHolder(binding)
    }
    interface RegionClicked {
        fun onRegionClicked(region: Region)
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem!!)
    }
}