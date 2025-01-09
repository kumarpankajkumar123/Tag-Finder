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
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.Adaptor.HistoryAdapter
import com.example.tagfinderapp.Inteface.ApiInterface
import com.example.tagfinderapp.Model.VideoModel
import com.example.tagfinderapp.Network.RetrofitInstanse
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Repository.Repository
import com.example.tagfinderapp.Util.UserDatabase
import com.example.tagfinderapp.ViewModal.TagViewModelFactory
import com.example.tagfinderapp.ViewModal.TagsViewModel
import com.example.tagfinderapp.databinding.FragmentHistoryTagsBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.regex.Pattern

class HistoryTags : Fragment() {
    lateinit var binding : FragmentHistoryTagsBinding

    private lateinit var tagviewmodel: TagsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryTagsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = UserDatabase.generateOrCreatedId() // Get the unique user ID
        val historyList = UserDatabase.getHistoryData(userId) // Fetch the history data
        Log.e("userId",""+userId)
        Log.e("historyList",""+historyList)
        Log.e("userDataHistoryTag",""+UserDatabase.getUserData(userId))

        val recyclerView = binding.historyTagRecyclerview
        val historyAdapter = HistoryAdapter(historyList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = historyAdapter

    }
}
