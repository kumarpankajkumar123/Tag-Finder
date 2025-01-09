package com.example.tagfinderapp.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tagfinderapp.Adaptor.VideoAdaptor
import com.example.tagfinderapp.Inteface.ApiInterface
import com.example.tagfinderapp.Model.VideoModel
import com.example.tagfinderapp.Network.RetrofitInstanse
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Repository.Repository
import com.example.tagfinderapp.ViewModal.TagViewModelFactory
import com.example.tagfinderapp.ViewModal.TagsViewModel
import com.example.tagfinderapp.databinding.FragmentMoreBinding
import com.example.tagfinderapp.databinding.FragmentMoreVideoDetailsBinding
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class MoreVideoDetails : Fragment(),OnClickListener {
    lateinit var binding: FragmentMoreVideoDetailsBinding
    private lateinit var tagviewmodel: TagsViewModel
    private var videoId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreVideoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = RetrofitInstanse
        val repository = Repository(retrofit.getRetrofit().create(ApiInterface::class.java))
        val factory = TagViewModelFactory(repository)
        tagviewmodel = ViewModelProvider(this, factory).get(TagsViewModel::class.java)
//        val videoId = receivedString?.let { getVideoIdFromUrl(it) }

//        videoUrl = arguments?.getString("video_url")
        videoId = arguments?.getString("videoId")
        Log.e("morevideo video url", "" + videoId)

        if (videoId != null) {
            load_data(videoId.toString())
        }
    }
    private fun processdatatag(it: VideoModel) {
        binding.tittle.text = it.items.get(0).snippet.title
        binding.description.text = it.items.get(0).snippet.description
        binding.date.text = it.items.get(0).snippet.publishedAt
        binding.channelId.text = it.items.get(0).snippet.channelId
        binding.videoId.text = it.items.get(0).id
        binding.channelTittle.text = it.items.get(0).snippet.channelTitle
    }
    override fun onClick(view: View?) {
    }
    fun load_data(videoId : String){
        tagviewmodel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        lifecycleScope.launch {
            tagviewmodel.getData(
                "snippet",
                videoId,
                "AIzaSyCrdo85_ezkyP0tMC-rC52Hlpr2qPjD7E8"
            )
            tagviewmodel.tags.observe(viewLifecycleOwner, Observer {
                it.onSuccess {
                    processdatatag(it)
                }
                it.onFailure {
                    Toast.makeText(requireContext(), "on failure methods", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }

}