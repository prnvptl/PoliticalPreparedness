package com.example.android.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.data.network.models.Channel
import com.example.android.politicalpreparedness.databinding.RepresentativeRowItemBinding
import com.example.android.politicalpreparedness.representative.model.Representative


class RepresentativeListAdapter :
    ListAdapter<Representative, RepresentativeListAdapter.RepresentativeViewHolder>(
        RepresentativeDiffCallback
    ) {

    class RepresentativeViewHolder(val binding: RepresentativeRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(representative: Representative) {
            binding.apply {
                rep = representative
                repTitle.text = representative.office.name
                repName.text = representative.official.name
                repParty.text = representative.official.party
            }
            showSocialLinks(representative.official.channels)
            showWWWLinks(representative.official.urls)
            binding.executePendingBindings()
        }

        private fun showSocialLinks(channels: List<Channel>?) {
            if (channels != null) {
                val facebookUrl = getFacebookUrl(channels)
                if (!facebookUrl.isNullOrBlank()) {
                    enableLink(binding.fbImg, facebookUrl)
                }

                val twitterUrl = getTwitterUrl(channels)
                if (!twitterUrl.isNullOrBlank()) {
                    enableLink(binding.twitterImg, twitterUrl)
                }
            }
        }

        private fun showWWWLinks(urls: List<String>?) {
            if (urls != null) {
                enableLink(binding.webImg, urls.first())
            }
        }

        private fun getFacebookUrl(channels: List<Channel>): String? {
            return channels.filter { channel -> channel.type == "Facebook" }
                .map { channel -> "https://www.facebook.com/${channel.id}" }
                .firstOrNull()
        }

        private fun getTwitterUrl(channels: List<Channel>): String? {
            return channels.filter { channel -> channel.type == "Twitter" }
                .map { channel -> "https://www.twitter.com/${channel.id}" }
                .firstOrNull()
        }

        private fun enableLink(view: ImageView, url: String) {
            view.visibility = View.VISIBLE
            view.setOnClickListener { setIntent(url) }
        }

        private fun setIntent(url: String) {
            val uri = Uri.parse(url)
            val intent = Intent(ACTION_VIEW, uri)
            itemView.context.startActivity(intent)
        }
    }

    object RepresentativeDiffCallback : DiffUtil.ItemCallback<Representative>() {
        override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
            return oldItem.official.name == newItem.official.name
        }

        override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        val binding = RepresentativeRowItemBinding.inflate(LayoutInflater.from(parent.context))
        binding.root.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return RepresentativeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val rep = getItem(position)
        holder.bind(rep)
    }
}