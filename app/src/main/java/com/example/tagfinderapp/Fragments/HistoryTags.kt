package com.example.tagfinderapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tagfinderapp.Adaptor.HistoryAdapter
import com.example.tagfinderapp.Adaptor.HistoryViewPager2Adapter
import com.example.tagfinderapp.Model.HistoryImageVideoIdModel
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Util.UserDatabase
import com.example.tagfinderapp.ViewModal.TagsViewModel
import com.example.tagfinderapp.databinding.FragmentHistoryTagsBinding

class HistoryTags : Fragment() {
    lateinit var binding: FragmentHistoryTagsBinding
    lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryTagsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jsonArray = UserDatabase.getVideoList()
        val historyList = mutableListOf<HistoryImageVideoIdModel>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val videoId = obj.getString("videoId")
            val thumbnail = obj.getString("thumbnail")
            val description = obj.getString("description")
            historyList.add(
                HistoryImageVideoIdModel(
                    thumbnailUrl = thumbnail,
                    videoId,
                    description
                )
            )
        }

        if (historyList.isEmpty()) {
            binding.noData.isVisible = true
            binding.historyTagRecyclerview.isVisible = false
        } else {
            binding.noData.isVisible = false
            binding.historyTagRecyclerview.isVisible = true
            binding.historyTagRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            adapter = HistoryAdapter(historyList) { videoId ->
                val bundle = Bundle()
                bundle.putString("historyVideoId", videoId)
                findNavController().navigate(R.id.historyFragment_to_tagsFragment, bundle)
            }
            binding.historyTagRecyclerview.adapter = adapter
        }
    }
}
