package com.example.tagfinderapp.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
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
import com.example.tagfinderapp.databinding.FragmentThumbnailBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class Thumbnail : Fragment() {

    lateinit var binding: FragmentThumbnailBinding
    private lateinit var tagviewmodel: TagsViewModel
    private var videoId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentThumbnailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = RetrofitInstanse
        val repository = Repository(retrofit.getRetrofit().create(ApiInterface::class.java))
        val factory = TagViewModelFactory(repository)
        tagviewmodel = ViewModelProvider(this, factory).get(TagsViewModel::class.java)

//        videoUrl = arguments?.getString("video_url")
        videoId = arguments?.getString("videoId")
        Log.e("Thumbnail videourl", "" + videoId)

        if(videoId != null){
            loadData(videoId.toString())
        }
    }

    private fun processdatatag(it: VideoModel) {
//        recycler = binding.tagsRecyclerview
//        recycler.layoutManager =
//            LinearLayoutManager(requireContext())
//        val allTags = mutableListOf<String>()
//        for (item in it.items) {
//            item.snippet.tags?.let { tags ->
//                allTags.addAll(tags) // Add all tags from this item
//            }
//        }
//        recycler.adapter = VideoAdaptor(requireContext(),allTags)

        Picasso.get()
            .load(it.items.get(0).snippet.thumbnails.standard.url)
            .into(binding.thumbnail)

        binding.description.text = it.items.get(0).snippet.title.toString()

//        binding.tagText.text = it.items.get(0).snippet.thumbnails.standard.height.toString() + "X" +it.items.get(0).snippet.thumbnails.standard.width.toString()
//        binding.tagTexth.text = it.items.get(0).snippet.thumbnails.high.height.toString() + "X" +it.items.get(0).snippet.thumbnails.high.width.toString()
//        binding.tagTextm.text = it.items.get(0).snippet.thumbnails.medium.height.toString() + "X" +it.items.get(0).snippet.thumbnails.medium.width.toString()

    }
    fun loadData(vid : String){
        tagviewmodel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        lifecycleScope.launch {
                tagviewmodel.getData("snippet",
                    vid,
                    "AIzaSyCrdo85_ezkyP0tMC-rC52Hlpr2qPjD7E8")
            }
            tagviewmodel.tags.observe(viewLifecycleOwner, Observer {
                it.onSuccess {
                    processdatatag(it)
                }
                it.onFailure {
                    Toast.makeText(requireContext(), "on failure methods", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
