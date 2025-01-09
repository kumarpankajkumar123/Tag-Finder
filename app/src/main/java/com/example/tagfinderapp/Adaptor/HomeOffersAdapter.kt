package com.example.tagfinderapp.Adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Model.ImageModel

class HomeOffersAdapter(
    private val context: Context,
    private val imageList: List<ImageModel>
) : RecyclerView.Adapter<HomeOffersAdapter.HomeOffersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.showimage, parent, false)
        return HomeOffersViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int = imageList.size

    class HomeOffersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val offerImage: ImageView = itemView.findViewById(R.id.offerImage)

        fun bind(imageModel: ImageModel) {
            offerImage.setImageResource(imageModel.imageRes)
        }
    }
}
