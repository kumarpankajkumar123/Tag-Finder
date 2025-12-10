package com.example.tagfinderapp.Adaptor

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.Model.HistoryImageVideoIdModel
import com.example.tagfinderapp.R
import com.squareup.picasso.Picasso
import org.json.JSONArray

class HistoryAdapter(
    private val historyList: List<HistoryImageVideoIdModel>,
    val onItemClick: (String) -> Unit
):
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoUrlTextView: TextView = view.findViewById(R.id.description)
        val thumbnailImageView: ImageView = view.findViewById(R.id.thumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.thumnaildesign, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        val videoId = item.videoId // Replace with the actual property name
        val thumbnailUrl = item.thumbnailUrl
        val description = item.description
        Log.d("historyThumbnail", "onBindViewHolder: ${thumbnailUrl}")
        Picasso.get().load(thumbnailUrl).into(holder.thumbnailImageView)
        holder.videoUrlTextView.isVisible = true
        holder.videoUrlTextView.text = description

        holder.itemView.setOnClickListener {
            if(videoId != null){
                onItemClick(videoId)
            }
        }
    }

    override fun getItemCount() = historyList.size
}
