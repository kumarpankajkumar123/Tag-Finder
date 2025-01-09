package com.example.tagfinderapp.Adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.Fragments.Thumbnail
import com.example.tagfinderapp.Model.Thumbnails
import com.example.tagfinderapp.R
import com.squareup.picasso.Picasso

class ThumbnailAdaptor(val context : Context, val thumbnail : List<Thumbnails>, val targetimage : ImageView) :
    RecyclerView.Adapter<ThumbnailAdaptor.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.thumnaildesign,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ThumbnailAdaptor.MyViewHolder, position: Int) {

        val thumbnail = thumbnail[position]

        holder.text1.text = thumbnail.high.width.toString()

        Picasso.get()
            .load(thumbnail.standard.url)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
    return thumbnail.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text1 : TextView = itemView.findViewById(R.id.tag_text)
//        val text2 : TextView = itemView.findViewById(R.id.description)
        val imageView : ImageView = itemView.findViewById(R.id.thumbnail)
    }
}