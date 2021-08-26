package com.example.g_maintain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.g_maintain.databinding.RvVisitsBinding
import com.example.g_maintain.db.MyDatabase
import com.example.g_maintain.db.Visit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VisitAdapter(
    private val visitClicked: VisitClicked
) : PagingDataAdapter<Visit, VisitAdapter.VisitViewHolder>(MyCallback()) {
    inner class VisitViewHolder(private val binding: RvVisitsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val visit = getItem(position)
                    if (visit != null) {
                        visitClicked.onVisitClicked(visit)
                    }
                }
            }
        }

        fun bind(visit: Visit) {
            binding.apply {
                txtRvVisitName.text = visit.name
                txtRvPrice.text = visit.price.toString()
                txtRvDate.text = visit.date
                switchRvStatus.isChecked = visit.done
                txtRvVisitClientName.visibility = View.GONE
            }

        }
    }

    class MyCallback : DiffUtil.ItemCallback<Visit>() {

        override fun areItemsTheSame(oldItem: Visit, newItem: Visit) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Visit, newItem: Visit) = oldItem == newItem

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VisitViewHolder {
        val binding =
            RvVisitsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VisitViewHolder(binding)
    }

    interface VisitClicked {
        fun onVisitClicked(visit: Visit)
    }

    override fun onBindViewHolder(holder: VisitViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}