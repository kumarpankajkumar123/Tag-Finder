package com.example.tagfinderapp.Fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.tagfinderapp.Adaptor.HomeOffersAdapter
import com.example.tagfinderapp.Adaptor.TodayVideoAdaptor
import com.example.tagfinderapp.Model.ImageModel
import com.example.tagfinderapp.Model.TodayVideo
import com.example.tagfinderapp.Network.ApiHandler
import com.example.tagfinderapp.Network.RetrofitInstanse
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Repository.Repository
import com.example.tagfinderapp.Util.HorizontalSpacingItemDecoration
import com.example.tagfinderapp.Util.ProgressDialog
import com.example.tagfinderapp.Util.UserDatabase
import com.example.tagfinderapp.ViewModal.VideoViewModel
import com.example.tagfinderapp.ViewModal.VideoViewModelFactory
import com.example.tagfinderapp.appConst.AppConst
import com.example.tagfinderapp.databinding.FragmentVideoDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.regex.Pattern


class VideoDetail : Fragment(), OnClickListener {

    lateinit var recycler: RecyclerView
    lateinit var binding: FragmentVideoDetailBinding
    private lateinit var mainViewModel: VideoViewModel
    private var snapHelper: LinearSnapHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentVideoDetailBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val retrofit = RetrofitInstanse.getRetrofit()
        val repository = Repository(retrofit)
        val factory = VideoViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this, factory).get(VideoViewModel::class.java)

        UserDatabase.init(requireContext())
        snapHelper = LinearSnapHelper()

//        refreshData()

        mainViewModel.getTodayVideo(
            AppConst.snippet,
            AppConst.chart,
            AppConst.Api_Key,
            AppConst.regionCode,
            AppConst.maxResult
        )

        mainViewModel._todayVideo.observe(viewLifecycleOwner) { status ->
            when (status) {
                is ApiHandler.Loading -> ProgressDialog.show(requireContext())

                is ApiHandler.Success -> {
                    ProgressDialog.dismiss()
                    val response = status.data
                    processdata(response)
                }

                is ApiHandler.Failure -> {
                    ProgressDialog.dismiss()
                    Toast.makeText(requireContext(), "no data found", Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.searchbtn.setOnClickListener(this)
        binding.gotoYoutube.setOnClickListener(this)
        binding.watchTutorial.setOnClickListener(this)
    }

    fun processdata(data: TodayVideo) {

        Log.e("tittle", ":=" + data.items.get(0).snippet.title)

        recycler = binding.hrRecycler
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = null
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = TodayVideoAdaptor(requireContext(), data) { videoId ->
            navigateToSubtagsFragment(videoId)
        }
        recycler.addItemDecoration(HorizontalSpacingItemDecoration(50))
        attachSnapHelper(recycler)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.searchbtn.id -> {
                val getvalue = binding.searchEditText.text.toString()

                if (getvalue.isEmpty()) {
                    Toast.makeText(requireContext(), "please enter video url", Toast.LENGTH_SHORT)
                        .show()
                } else if (IsValidUrl(getvalue)) {
                    val bundle = Bundle()
                    bundle.putString("video_url", getvalue) // Pass the string

                    findNavController().navigate(R.id.videoFragment_to_tagsFragment, bundle)
                    Log.d("FragmentNavigation", "VideoDetail Fragment: Navigated to TagFragment")


                } else {
                    Toast.makeText(
                        requireContext(),
                        "please enter correct video url",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            binding.gotoYoutube.id -> {
                // Open the YouTube app or URL
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://www.youtube.com") // YouTube URL
                    `package` = "com.google.android.youtube" // Open in YouTube app if available
                }

                // Check if the YouTube app is available
                if (intent.resolveActivity(requireContext().packageManager) != null) {
                    startActivity(intent)
                } else {
                    // Fallback to opening in a browser
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
                    startActivity(browserIntent)
                }
            }

            binding.watchTutorial.id -> {
                findNavController().navigate(R.id.videoFragment_to_appTutorialFragment)
            }
        }
    }

    private fun IsValidUrl(url: String): Boolean {
        val YOUTUBE_URL_PATTERN =
            "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu\\.be))(\\/((watch\\?v=|embed\\/|v\\/|shorts\\/|live\\/)?([\\w\\-]{11}))(\\S*)?)?$";
        if (TextUtils.isEmpty(url)) {
            return false
        }

        // Check if there are spaces or multiple URLs (we allow only one URL)
        if (url.contains(" ") || url.contains(",")) {
            return false
        }

        // Check if the URL is a valid web URL
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            return false
        }
        // Check if the URL matches the YouTube URL pattern
        val pattern = Pattern.compile(YOUTUBE_URL_PATTERN)

        //Pattern pattern = Pattern.compile(YOUTUBE_URL_PATTERNNEW);
        return pattern.matcher(url).matches()
    }



    fun attachSnapHelper(recyclerView: RecyclerView) {
        if (snapHelper == null) {
            snapHelper = LinearSnapHelper().apply {
                attachToRecyclerView(recyclerView)
            }
        }
    }


    private fun navigateToSubtagsFragment(videoId: String) {

        Log.e("videodetail videoid", "" + videoId)

        val bundle = Bundle()
        bundle.putString("todayVideoId", videoId)
        Log.d("videoId", "navigateToSubtagsFragment: ${videoId}")


        val navController = findNavController()
        navController.navigate(R.id.videoFragment_to_tagsFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        binding.searchEditText.text?.clear()
    }
}

