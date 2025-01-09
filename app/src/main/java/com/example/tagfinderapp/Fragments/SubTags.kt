package com.example.tagfinderapp.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.Adaptor.TodayVideoAdaptor
import com.example.tagfinderapp.Adaptor.VideoAdaptor
import com.example.tagfinderapp.Inteface.ApiInterface
import com.example.tagfinderapp.Model.VideoModel
import com.example.tagfinderapp.Network.RetrofitInstanse
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Repository.Repository
import com.example.tagfinderapp.ViewModal.TagViewModelFactory
import com.example.tagfinderapp.ViewModal.TagsViewModel
import com.example.tagfinderapp.databinding.FragmentSubTagsBinding
import com.example.tagfinderapp.databinding.FragmentThumbnailBinding
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SubTags : Fragment() {
    private lateinit var adapter: VideoAdaptor
    lateinit var recycler: RecyclerView
    lateinit var binding: FragmentSubTagsBinding
    private lateinit var tagviewmodel: TagsViewModel
    private var videoId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubTagsBinding.inflate(inflater, container, false)
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
        Log.e("SubTags video id", "" + videoId)

        if(videoId != null){
            loadData(videoId.toString())
        }

//        val adapter = VideoAdaptor(requireContext(), listOf()) // Pass the real list later
//        binding.tagsRecyclerview.adapter = adapter
//        binding.tagsRecyclerview.layoutManager = LinearLayoutManager(requireContext())

//        val adapter = VideoAdaptor(requireContext(), listOf()) { isAnyChecked ->
//            // Callback for checkbox state
//            (parentFragment as? TagsFragmant)?.onSubTagsCheckBoxStateChanged(isAnyChecked)
//        }

    }


    private fun processdatatag(it: VideoModel) {
        recycler = binding.tagsRecyclerview
        recycler.layoutManager =
            LinearLayoutManager(requireContext())
        val allTags = mutableListOf<String>()
        for (item in it.items) {
            item.snippet.tags?.let { tags ->
                allTags.addAll(tags) // Add all tags from this item
            }
        }
        adapter = VideoAdaptor(requireContext(), allTags) { isAnyChecked ->
            // Callback for checkbox state
            (parentFragment as? TagsFragmant)?.onSubTagsCheckBoxStateChanged(isAnyChecked)
        }
        recycler.adapter = adapter

    }
    fun loadData(videoId : String){
        tagviewmodel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        Log.e("loadData","subTag hua")
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

    fun clearAllCheckboxes() {
        adapter.clearSelection() // Clear all selections in the adapter
    }

    fun copySelectedTags() {
        val adapter = binding.tagsRecyclerview.adapter as? VideoAdaptor
        adapter?.copySelectedTags()
    }

    fun selectAllTags() {
        val adapter = binding.tagsRecyclerview.adapter as? VideoAdaptor
        adapter?.selectAllTags()
    }

    fun copyAllTags() {
        val adapter = binding.tagsRecyclerview.adapter as? VideoAdaptor
        adapter?.selectAllTags() // Select all checkboxes first
        adapter?.copyAllTags()   // Copy all tags
    }

}