package com.example.tagfinderapp.Adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.R
import com.squareup.picasso.Picasso

class HistoryAdapter(private val historyList: List<Pair<String, String>>) :
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
        val (videoUrl, thumbnail) = historyList[position]
//        holder.videoUrlTextView.text = videoUrl
        Picasso.get().load(thumbnail).into(holder.thumbnailImageView)
        holder.videoUrlTextView.visibility = View.GONE
    }

    override fun getItemCount() = historyList.size
}
