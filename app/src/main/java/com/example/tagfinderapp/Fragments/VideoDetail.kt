package com.example.tagfinderapp.Fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.tagfinderapp.Adaptor.HomeOffersAdapter
import com.example.tagfinderapp.Adaptor.TodayVideoAdaptor
import com.example.tagfinderapp.Inteface.ApiInterface
import com.example.tagfinderapp.Model.ImageModel
import com.example.tagfinderapp.Model.TodayVideo
import com.example.tagfinderapp.Network.RetrofitInstanse
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Repository.Repository
import com.example.tagfinderapp.Util.HorizontalSpacingItemDecoration
import com.example.tagfinderapp.Util.UserDatabase
import com.example.tagfinderapp.ViewModal.VideoViewModelFactory
import com.example.tagfinderapp.ViewModal.VideoViewModel
import com.example.tagfinderapp.databinding.FragmentVideoDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.util.regex.Pattern


class VideoDetail : Fragment(), OnClickListener {

    lateinit var recycler: RecyclerView
    private var _binding: FragmentVideoDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: VideoViewModel
    lateinit var snapHelper: LinearSnapHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentVideoDetailBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val retrofit = RetrofitInstanse
        val repository = Repository(retrofit.getRetrofit().create(ApiInterface::class.java))
        val factory = VideoViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this, factory).get(VideoViewModel::class.java)

        UserDatabase.init(requireContext())
        snapHelper = LinearSnapHelper()

//        refreshData()
        mainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        lifecycleScope.launch {
            mainViewModel.getTodayVideo(
                "snippet",
                "mostpopular",
                "AIzaSyCrdo85_ezkyP0tMC-rC52Hlpr2qPjD7E8",
                "IN",
                20
            )
            mainViewModel._todayVideo.observe(viewLifecycleOwner, Observer {
                it.onSuccess {
                    processdata(it)
                }
                it.onFailure {
                    if (it.cause is SocketTimeoutException) {
                        Log.e("API Error", "Request timed out!")
                        Toast.makeText(requireContext(),"please check your internet",Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("API Error", "Other error: ${it.message}")
                        Toast.makeText(requireContext(),"please restart the application",Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        binding.searchbtn.setOnClickListener(this)
        binding.gotoYoutube.setOnClickListener(this)
        binding.watchTutorial.setOnClickListener(this)
    }

    fun processdata(data: TodayVideo) {
//        binding.demo.text = data.items.get(0).snippet.title
        Log.e("tittle", ":=" + data.items.get(0).snippet.title)

        recycler = binding.hrRecycler
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = null
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = TodayVideoAdaptor(requireContext(), data){ videoId ->
            navigateToSubtagsFragment(videoId)
        }
        recycler.addItemDecoration(HorizontalSpacingItemDecoration(50))
        attachSnapHelper(recycler)

    }
    override fun onClick(view: View?) {
        when (view?.id) {
            binding.searchbtn.id -> {
                val getvalue = binding.searchEditText.text.toString()
                val userId = UserDatabase.generateOrCreatedId()
                Log.e("VideofragmentId", "" + userId)

                if (getvalue.isEmpty()) {
                    Toast.makeText(requireContext(), "please enter video url", Toast.LENGTH_SHORT)
                        .show()
                } else if (IsValidUrl(getvalue)) {

                    UserDatabase.addUrl(getvalue, userId)
                    Log.e("videoFragment userdata", "" + UserDatabase.getUserData(userId))

                    binding.searchbtn.visibility = GONE
                    binding.searchEditText.visibility = GONE
                    binding.watchTutorial.visibility = GONE
                    binding.gotoYoutube.visibility = GONE
                    binding.demo.visibility = GONE
                    binding.hrRecycler.visibility = GONE

                    val childFragment = TagsFragmant()
                    val bundle = Bundle()

                    bundle.putString("video_url", getvalue) // Pass the string
                    childFragment.arguments = bundle // Set arguments
//                    Log.e("fragment url", "" + getvalue)

                    childFragmentManager.beginTransaction()
                        .replace(R.id.insideVideoFramlayout, childFragment) // Use the same instance
                        .addToBackStack(null)
                        .commit()
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
                showImageDialog()
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
    private fun showImageDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialogiscreenshot, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val viewPager = dialogView.findViewById<ViewPager2>(R.id.photos_viewpager)
        val dotsContainer = dialogView.findViewById<TabLayout>(R.id.tab_layout)

        // List of images
        val imageList = listOf(
            ImageModel(R.drawable.first),
            ImageModel(R.drawable.second),
            ImageModel(R.drawable.third)
        )

        // Set up the adapter for ViewPager2
        val adapter1 = HomeOffersAdapter(requireContext(), imageList)
        viewPager.adapter = adapter1

        // Attach TabLayout to ViewPager2 for dots
        TabLayoutMediator(dotsContainer, viewPager) { tab, position ->
            // Custom labels for tabs (optional)
        }.attach()

        dialog.show()

    }
    fun attachSnapHelper(recyclerView: RecyclerView) {
        if (snapHelper == null) {
            snapHelper = LinearSnapHelper().apply {
                attachToRecyclerView(recyclerView)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // Clean up the binding reference
    }
    override fun onDestroy() {
        super.onDestroy()
    }
    private fun navigateToSubtagsFragment(videoId: String) {

        Log.e("videodetail videoid",""+videoId)
        binding.searchbtn.visibility = GONE
        binding.searchEditText.visibility = GONE
        binding.watchTutorial.visibility = GONE
        binding.gotoYoutube.visibility = GONE
        binding.demo.visibility = GONE
        binding.hrRecycler.visibility = GONE

        val childFragment = TagsFragmant()
        val bundle = Bundle()
        bundle.putString("todayVideoId", videoId)
        bundle.putString("boolean", "true")
        childFragment.arguments = bundle // Set arguments
        childFragmentManager.beginTransaction()
            .replace(R.id.insideVideoFramlayout, childFragment) // Use the same instance
            .addToBackStack(null)
            .commit()
    }
}

