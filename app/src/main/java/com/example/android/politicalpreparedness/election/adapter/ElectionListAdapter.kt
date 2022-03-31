package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.databinding.ElectionRowItemBinding


class ElectionListAdapter :
    ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        val binding = ElectionRowItemBinding.inflate(LayoutInflater.from(parent.context))
        return ElectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val electionItem = getItem(position)
        holder.bind(electionItem)
    }

    companion object ElectionDiffCallBack : DiffUtil.ItemCallback<Election>() {
        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem == newItem
        }
    }

    class ElectionViewHolder(private val electionRowItemBinding: ElectionRowItemBinding) :
        RecyclerView.ViewHolder(electionRowItemBinding.root) {
        fun bind(item: Election) {
            electionRowItemBinding.apply {
                title.text = item.name
                time.text = item.electionDay.toString()
            }
        }
    }
}