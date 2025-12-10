package com.example.tagfinderapp.Adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.Model.ItemXX
import com.example.tagfinderapp.databinding.CompetitorItemBinding
import com.squareup.picasso.Picasso

class CompetitorAdapter(
    val context: Context,
    val list: List<ItemXX>,
    val videoIdCallBack :(String) -> Unit
) : RecyclerView.Adapter<CompetitorAdapter.CompetitorClass>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompetitorAdapter.CompetitorClass {
        val binding =
            CompetitorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CompetitorClass(binding)
    }

    override fun onBindViewHolder(holder: CompetitorAdapter.CompetitorClass, position: Int) {
        val item = list[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CompetitorClass(private val binding: CompetitorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemXX) {
            val imageUrl = item.snippet?.thumbnails?.medium?.url
            if (imageUrl != null) {
                binding.competitorCard.isVisible = true
                Picasso.get().load(imageUrl)
                    .into(binding.competitorImage)
            }
            val title = item.snippet?.title
            val des = item.snippet?.description

            if (title != null) {
                binding.title.text = title
            }
            if (des != null) {
                binding.description.text = des
            }

            binding.mainLayout.setOnClickListener {
                val videoId = item.id?.videoId
                if(videoId != null){
                    videoIdCallBack(videoId)
                }
            }
        }
    }
}