package com.example.g_maintain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.g_maintain.databinding.RvClientRegionBinding
import com.example.g_maintain.db.Client
import com.example.g_maintain.db.Contract

class ContractAdapter(private val contractClicked: ContractClicked) : PagingDataAdapter<Contract, ContractAdapter.ContractViewHolder>(MyCallback()) {

    inner class ContractViewHolder(private val binding: RvClientRegionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contract = getItem(position)
                    if (contract != null) {
                        contractClicked.onClientClicked(contract)
                    }
                }
            }
        }
        fun bind(contract: Contract) {
            binding.txtRvClientRegion.text = contract.name
        }
    }

    class MyCallback : DiffUtil.ItemCallback<Contract>() {
        override fun areItemsTheSame(oldItem: Contract, newItem: Contract) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Contract, newItem: Contract) = oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractViewHolder {
        val binding =
            RvClientRegionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContractViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContractViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem!!)
    }

    interface ContractClicked {
        fun onClientClicked(contract: Contract)
    }
}