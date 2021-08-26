package com.example.g_maintain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.g_maintain.databinding.RvClientRegionBinding
import com.example.g_maintain.db.Client

class ClientAdapter(private val clientClicked: ClientClicked) : PagingDataAdapter<Client, ClientAdapter.ClientViewHolder>(MyCallback()) {

    inner class ClientViewHolder(private val binding: RvClientRegionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val client = getItem(position)
                    if (client != null) {
                        clientClicked.onClientClicked(client)
                    }
                }
            }
        }
        fun bind(client: Client) {
            binding.txtRvClientRegion.text = client.name
        }
    }

    class MyCallback : DiffUtil.ItemCallback<Client>() {
        override fun areItemsTheSame(oldItem: Client, newItem: Client) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Client, newItem: Client) = oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val binding =
            RvClientRegionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem!!)
    }

    interface ClientClicked {
        fun onClientClicked(client: Client)
    }
}