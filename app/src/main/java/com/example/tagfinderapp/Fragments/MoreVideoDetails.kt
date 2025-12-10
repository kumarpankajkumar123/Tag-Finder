package com.example.tagfinderapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.tagfinderapp.Model.VideoModel
import com.example.tagfinderapp.databinding.FragmentMoreVideoDetailsBinding

class MoreVideoDetails(val todayModel: VideoModel) : Fragment(), OnClickListener {
    lateinit var binding: FragmentMoreVideoDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreVideoDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = todayModel.items.firstOrNull()
        val title = item?.snippet?.title
        val des = item?.snippet?.description
        val date = item?.snippet?.publishedAt
        val channelId = item?.snippet?.channelId
        val channelTitle = item?.snippet?.channelTitle
        val videoId = item?.id


        if (title != null) {
            binding.videoTittle.isVisible = true
            binding.tittle.text = title
        }

        if (des != null) {
            binding.videoDescription.isVisible = true
            binding.description.text = des
        }

        if (date != null || channelTitle != null || channelId != null || videoId != null) {
            binding.channelDetail.isVisible = true

            if (date != null) {
                binding.date.isVisible = true
                binding.date.text = date
            }

            if (channelTitle != null) {
                binding.channelTittle.isVisible = true
                binding.channelTittle.text = channelTitle
            }

            if (channelId != null) {
                binding.channelId.isVisible = true
                binding.channelId.text = channelId
            }

            if (videoId != null) {
                binding.videoId.isVisible = true
                binding.videoId.text = videoId
            }
        } else {
            binding.channelDetail.isVisible = false
        }


    }

    override fun onClick(view: View?) {
    }

}