package com.example.tagfinderapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tagfinderapp.Adaptor.KeywordsAdaptor
import com.example.tagfinderapp.Model.ItemXX
import com.example.tagfinderapp.Network.ApiHandler
import com.example.tagfinderapp.Network.RetrofitInstanse
import com.example.tagfinderapp.Repository.Repository
import com.example.tagfinderapp.Util.ProgressDialog
import com.example.tagfinderapp.ViewModal.KeywordsViewModelFctory
import com.example.tagfinderapp.ViewModal.KeyworsViewModel
import com.example.tagfinderapp.appConst.AppConst
import com.example.tagfinderapp.databinding.FragmentDataKeyWordsBinding

class DataKeyWords(val keyword: String) : Fragment() {
    lateinit var binding: FragmentDataKeyWordsBinding
    lateinit var competitorViewModel: KeyworsViewModel
    lateinit var adaptor: KeywordsAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDataKeyWordsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receivedString = arguments?.getString("keywords")
        Log.e("sub fragment keyword", "" + receivedString)

        val retrofit = RetrofitInstanse.getRetrofit()
        val repository = Repository(retrofit)
        val factory = KeywordsViewModelFctory(repository)
        competitorViewModel = ViewModelProvider(this, factory).get(KeyworsViewModel::class.java)

        binding.tagsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        competitorViewModel.getCompetitor(
            AppConst.snippet, AppConst.Api_Key, AppConst.twenty,
            AppConst.type_channel, keyword
        )

        competitorViewModel.competitorData.observe(viewLifecycleOwner) { status ->
            when (status) {
                is ApiHandler.Loading -> {
                    ProgressDialog.show(requireContext())
                }

                is ApiHandler.Success -> {
                    ProgressDialog.dismiss()
                    val list = status.data?.items
                    if (list != null) {
                        setupRecyclerview(list)
                    }
                }

                is ApiHandler.Failure -> {
                    ProgressDialog.dismiss()
                }
            }
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

    fun setupRecyclerview(list: List<ItemXX>) {
        adaptor = KeywordsAdaptor(requireContext(), list){ isAnyChecked ->
            if (isAnyChecked) {
                binding.content.isVisible = true
            } else {
                binding.content.isVisible = false
            }
        }
        binding.tagsRecyclerview.adapter = adaptor
    }

    fun clearAllCheckboxes() {
        adaptor.clearSelection() // Clear all selections in the adapter
    }

    fun copySelectedTags() {
        adaptor.copySelectedTags()
    }

    fun selectAllTags() {
        adaptor.selectAllTags()
    }

    fun copyAllTags() {
        adaptor.selectAllTags() // Select all checkboxes first
        adaptor.copyAllTags()   // Copy all tags
    }

}