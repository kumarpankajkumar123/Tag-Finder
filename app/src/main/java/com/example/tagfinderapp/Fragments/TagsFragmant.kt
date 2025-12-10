package com.example.tagfinderapp.Fragments

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tagfinderapp.Adaptor.SubChildPagerAdapter
import com.example.tagfinderapp.Model.VideoModel
import com.example.tagfinderapp.Network.ApiHandler
import com.example.tagfinderapp.Network.RetrofitInstanse
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Repository.Repository
import com.example.tagfinderapp.Util.ProgressDialog
import com.example.tagfinderapp.Util.UserDatabase
import com.example.tagfinderapp.ViewModal.TagViewModelFactory
import com.example.tagfinderapp.ViewModal.TagsViewModel
import com.example.tagfinderapp.appConst.AppConst
import com.example.tagfinderapp.databinding.CustomTabLayoutBinding
import com.example.tagfinderapp.databinding.FragmentTagsFragmantBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class TagsFragmant : Fragment() {
    lateinit var binding: FragmentTagsFragmantBinding
    private lateinit var tagviewmodel: TagsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTagsFragmantBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoUrl = arguments?.getString("video_url") ?: ""
        val imageClickVideoId = arguments?.getString("todayVideoId") ?: ""
        val competitorVideoId = arguments?.getString("competitorVideoId") ?: ""
        val historyVideoId = arguments?.getString("historyVideoId") ?: ""

        val retrofit = RetrofitInstanse.getRetrofit()
        val repository = Repository(retrofit)
        val factory = TagViewModelFactory(repository)
        tagviewmodel = ViewModelProvider(this, factory).get(TagsViewModel::class.java)

        if (videoUrl.isNotEmpty()) {
            val getVideoId = getVideoIdFromUrl(videoUrl)
            Log.e("videoId getVideoId", "" + getVideoId)
            if (getVideoId != null) {
                load_data(getVideoId)
            }
        }

        if (imageClickVideoId.isNotEmpty()) {
            Log.d("videoId imageClicked", "onViewCreated: ${imageClickVideoId}")
            load_data(imageClickVideoId)
        }

        if (competitorVideoId.isNotEmpty()) {
            Log.d("videoId imageClicked", "onViewCreated: ${competitorVideoId}")
            load_data(competitorVideoId)
        }

        if (historyVideoId.isNotEmpty()) {
            Log.d("videoId imageClicked", "onViewCreated: ${historyVideoId}")
            load_data(historyVideoId)
        }

        binding.backarrow.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun processdatatag(it: VideoModel) {

        UserDatabase.saveVideo(it.items.firstOrNull()?.id?:"", it.items.firstOrNull()?.snippet?.thumbnails?.high?.url?:"",it.items.firstOrNull()?.snippet?.description?:"")

        val tittle = it.items.firstOrNull()?.snippet?.title ?: ""
        Log.e("tittle", "" + tittle)
        binding.keyword.text = tittle

        val pagerAdapter = SubChildPagerAdapter(this, it)
        binding.subchildViewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.subchildViewPager) { tab, position ->
            val customView = CustomTabLayoutBinding.inflate(LayoutInflater.from(requireContext()))
            customView.tabText.text = when (position) {
                0 -> AppConst.Tags
                1 -> AppConst.thumbnail
                2 -> AppConst.more
                else -> ""
            }
            val colorRes = if (position == 0) {
                R.color.purple_700
            } else {
                R.color.black
            }
            customView.tabText.setTextColor(
                ContextCompat.getColor(requireContext(), colorRes)
            )
            tab.customView =  customView.root
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.customView?.let { view ->
                    val customBinding = CustomTabLayoutBinding.bind(view)
                    customBinding.tabText.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.purple_700)
                    )
                    // optional: thoda bold bhi kar sakte:
                    customBinding.tabText.setTypeface(null, Typeface.BOLD)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.customView?.let { view ->
                    val customBinding = CustomTabLayoutBinding.bind(view)
                    customBinding.tabText.setTextColor(
                        ContextCompat.getColor(requireContext(), android.R.color.black)
                    )
                    // optional:
                    customBinding.tabText.setTypeface(null, Typeface.NORMAL)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }

    private fun getVideoIdFromUrl(getvalue: String): String? {
        Log.e("getid methods", ":= call hua")
        val regex =
            "(?:youtube\\.com\\/.*[?&]v=|youtu\\.be\\/|youtube\\.com\\/shorts\\/)?([\\w\\-]{11})"

        val pattern1 = Pattern.compile(regex)
        val macher1 = pattern1.matcher(getvalue)

        if (macher1.find()) {
            return macher1.group(1)
        } else {
            Toast.makeText(requireContext(), "please enter correct video url", Toast.LENGTH_SHORT)
                .show()
            return null
        }
    }

    fun load_data(videoId: String) {
        Log.e("load_data", "call hua")

        lifecycleScope.launch {
            tagviewmodel.getData(
                AppConst.snippet,
                videoId,
                AppConst.Api_Key
            )
            tagviewmodel.tags.observe(viewLifecycleOwner) { status ->
                when (status) {

                    is ApiHandler.Loading -> {
                        ProgressDialog.show(requireContext())
                    }
                    is ApiHandler.Success -> {
                        ProgressDialog.dismiss()
                        val res = status.data
                        processdatatag(res)
                    }

                    is ApiHandler.Failure -> {
                        ProgressDialog.dismiss()
                    }
                }
            }
        }
    }
}
