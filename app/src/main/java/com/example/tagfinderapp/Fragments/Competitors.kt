package com.example.tagfinderapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tagfinderapp.Adaptor.CompetitorAdapter
import com.example.tagfinderapp.Model.ItemXX
import com.example.tagfinderapp.Network.ApiHandler
import com.example.tagfinderapp.Network.RetrofitInstanse
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Repository.Repository
import com.example.tagfinderapp.Util.ProgressDialog
import com.example.tagfinderapp.ViewModal.KeywordsViewModelFctory
import com.example.tagfinderapp.ViewModal.KeyworsViewModel
import com.example.tagfinderapp.appConst.AppConst
import com.example.tagfinderapp.databinding.FragmentCompititorBinding


class Competitors(val keyword: String) : Fragment() {


    lateinit var binding: FragmentCompititorBinding
    lateinit var competitorViewModel: KeyworsViewModel
    lateinit var adapter: CompetitorAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompititorBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val retrofit = RetrofitInstanse.getRetrofit()
        val repository = Repository(retrofit)
        val factory = KeywordsViewModelFctory(repository)

        Log.d("keywords", "onViewCreated: ${keyword}")

        competitorViewModel = ViewModelProvider(this, factory)[KeyworsViewModel::class.java]
        binding.competitorRecycler.layoutManager = LinearLayoutManager(requireContext())

        competitorViewModel.getCompetitor(
            AppConst.snippet, AppConst.Api_Key, AppConst.twenty,
            AppConst.type, keyword
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
    }

    fun setupRecyclerview(list: List<ItemXX>) {

        adapter = CompetitorAdapter(requireContext(), list) { videoId ->
            val bundle = Bundle()
            bundle.putString("competitorVideoId", videoId)
            findNavController().navigate(R.id.competitor_to_tagsfragment, bundle)
        }
        binding.competitorRecycler.adapter = adapter
    }
}