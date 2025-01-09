package com.example.tagfinderapp.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.Adaptor.KeywordsAdaptor
import com.example.tagfinderapp.Adaptor.TodayVideoAdaptor
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Util.UserDatabase
import com.example.tagfinderapp.databinding.FragmentHistoryKeywordsBinding
import org.json.JSONObject

class HistoryKeywords : Fragment() {
    private var keywordsList: List<String>? = null
     lateinit var binding : FragmentHistoryKeywordsBinding
     lateinit var recycler : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryKeywordsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UserDatabase.init(requireContext())
        val userId = UserDatabase.generateOrCreatedId()
        Log.e("historykeywordId",""+userId)

        val userData = UserDatabase.getUserData(userId)
        Log.e("historykeywordData",""+userData)

        if (userData != null) {
            val jsonObject = JSONObject(userData.toString())
            keywordsList = UserDatabase.getKeywordsList(jsonObject)
        } else {
            Log.e("Error", "No user data found for userId: $userId")
        }
        Log.e("historykeywordList",""+keywordsList)

        recycler = binding.historyKywordRecycler
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler.adapter = keywordsList?.let { KeywordsAdaptor(requireContext(), it) }

    }
}