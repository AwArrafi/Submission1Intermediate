package com.example.submission1intermediate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission1intermediate.databinding.ItemStoryBinding
import com.example.submission1intermediate.model.Story

class StoryAdapter(
    private val storyList: List<Story>,
    private val onClick: (Story) -> Unit
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = storyList[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int = storyList.size

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) {
            binding.tvItemName.text = story.name
            binding.tvItemDescription.text = story.description

            Glide.with(binding.ivItemPhoto.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)

            // Set click listener
            itemView.setOnClickListener { onClick(story) }
        }
    }
}
