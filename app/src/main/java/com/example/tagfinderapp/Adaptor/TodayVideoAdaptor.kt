package com.example.tagfinderapp.Adaptor

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.Model.TodayVideo
import com.example.tagfinderapp.R
import com.squareup.picasso.Picasso

class TodayVideoAdaptor(
    val context: Context,
    var todayVideo: TodayVideo,
    private val onItemClick: (String) -> Unit
) :
    RecyclerView.Adapter<TodayVideoAdaptor.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.hr_recyclerview, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = todayVideo.items[position]
        Picasso.get()
            .load(list.snippet.thumbnails.high.url)
            .placeholder(R.drawable.tag)
            .error(R.drawable.wifi_off_svgrepo_com)
            .into(holder.imageView)

        holder.text1.text = list.snippet.title

        if (position == 0) {
            animateItem(holder.itemView)
        } else {
            holder.itemView.translationX = 0f // Reset animation for other items
        }

        holder.itemView.setOnClickListener {
            onItemClick(list.id)
        }


    }

    override fun getItemCount(): Int {
        return todayVideo.items.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.thumbnail)
        val text1: TextView = itemView.findViewById(R.id.description)

    }

    private fun animateItem(view: View) {
        val screenWidth = view.resources.displayMetrics.widthPixels.toFloat()

        // Move from right to left and back
        val moveToCenter = ObjectAnimator.ofFloat(view, "translationX", screenWidth, 0f)
//        val moveBack = ObjectAnimator.ofFloat(view, "translationX", 0f, screenWidth)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(moveToCenter)
        animatorSet.duration = 800 // Total duration (adjust as needed)
        animatorSet.start()
    }
}