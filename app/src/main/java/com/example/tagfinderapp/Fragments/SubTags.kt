package com.example.tagfinderapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tagfinderapp.Adaptor.VideoAdaptor
import com.example.tagfinderapp.Model.VideoModel
import com.example.tagfinderapp.databinding.FragmentSubTagsBinding

class SubTags(val todayModel: VideoModel) : Fragment() {
    private lateinit var adapter: VideoAdaptor
    lateinit var binding: FragmentSubTagsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubTagsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val tagsList = todayModel.items.firstOrNull()?.snippet?.tags ?: emptyList()

        if (tagsList.isEmpty()) {
            binding.noData.isVisible = true
        } else {
            binding.noData.isVisible = false
            binding.tagsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            adapter = VideoAdaptor(requireContext(), tagsList) { isAnyChecked ->

                if (isAnyChecked) {
                    binding.content.isVisible = true
                } else {
                    binding.content.isVisible = false
                }
            }
            binding.tagsRecyclerview.adapter = adapter
        }

        binding.cancelall.setOnClickListener {
            clearAllCheckboxes()
        }

        binding.searchbtn.setOnClickListener {
            copySelectedTags()
        }

        binding.selecall.setOnClickListener {
            selectAllTags()
        }

        binding.copyall.setOnClickListener {
            copyAllTags()
        }
    }

    fun clearAllCheckboxes() {
        adapter.clearSelection() // Clear all selections in the adapter
    }

    fun copySelectedTags() {
        adapter.copySelectedTags()
    }

    fun selectAllTags() {
        adapter.selectAllTags()
    }

    fun copyAllTags() {
        adapter.selectAllTags() // Select all checkboxes first
        adapter.copyAllTags()   // Copy all tags
    }

}