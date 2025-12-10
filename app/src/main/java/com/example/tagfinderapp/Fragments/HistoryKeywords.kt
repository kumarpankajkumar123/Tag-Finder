package com.example.tagfinderapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.Adaptor.HistoryKeywordsAdapter
import com.example.tagfinderapp.Adaptor.KeywordsAdaptor
import com.example.tagfinderapp.Util.UserDatabase
import com.example.tagfinderapp.databinding.FragmentHistoryKeywordsBinding
import org.json.JSONObject

class HistoryKeywords : Fragment() {
    lateinit var binding: FragmentHistoryKeywordsBinding
    lateinit var adapter : HistoryKeywordsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryKeywordsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UserDatabase.init(requireContext())
        val keywordsList = UserDatabase.getKeywords()
        Log.e("keywordsList", "" + keywordsList)

        if(keywordsList.isEmpty()){
            binding.noData.isVisible = true
            binding.historyKywordRecycler.isVisible = false
        }else{
            binding.noData.isVisible = false
            binding.historyKywordRecycler.isVisible = true
            binding.historyKywordRecycler.layoutManager = LinearLayoutManager(requireContext())
            adapter = HistoryKeywordsAdapter(requireContext(),keywordsList)
            binding.historyKywordRecycler.adapter = adapter
        }
    }
}