package com.example.tagfinderapp.Fragments

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.Adaptor.TodayVideoAdaptor
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
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener


class VideoDetail : Fragment(), OnClickListener {

    lateinit var recycler: RecyclerView
    lateinit var binding: FragmentVideoDetailBinding
    private lateinit var todayAdapter: TodayVideoAdaptor

    private lateinit var mainViewModel: VideoViewModel
    private var snapHelper: LinearSnapHelper? = null
    private val interstitialTestUnitId = AppConst.interstialTextId
    private var interstitialAd: InterstitialAd? = null
    private var decorationAdded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(requireContext()) {}
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

        binding.swipeRefresh.setOnRefreshListener {
            mainViewModel.getTodayVideo(
                AppConst.snippet,
                AppConst.chart,
                AppConst.Api_Key,
                AppConst.regionCode,
                AppConst.maxResult
            )
        }

        mainViewModel._todayVideo.observe(viewLifecycleOwner) { status ->
            when (status) {
                is ApiHandler.Loading -> ProgressDialog.show(requireContext())
                is ApiHandler.Success -> {
                    ProgressDialog.dismiss()
                    binding.swipeRefresh.isRefreshing = false
                    val response = status.data
                    processdata(response)
                }
                is ApiHandler.Failure -> {
                    binding.swipeRefresh.isRefreshing = false
                    ProgressDialog.dismiss()
                }
            }
        }

        ApiCall()

        binding.searchBtn.setOnClickListener(this)
        binding.gotoYoutube.setOnClickListener(this)
        binding.watchTutorial.setOnClickListener(this)

        binding.searchEditText.addTextChangedListener {
            if (binding.errorText.isVisible) {
                binding.errorText.visibility = View.GONE
            }
        }


        val adView = AdView(requireContext())
        adView.adUnitId = AppConst.BannerUnitId
        adView.setAdSize(
            AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                requireContext(), 360
            )
        )
        binding.adViewContainer.removeAllViews()
        binding.adViewContainer.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        loadInterstitial()
    }

    private fun ApiCall(){
        mainViewModel.getTodayVideo(
            AppConst.snippet,
            AppConst.chart,
            AppConst.Api_Key,
            AppConst.regionCode,
            AppConst.maxResult
        )
    }

    private fun loadInterstitial() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            requireContext(),
            interstitialTestUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d("AdMob", "Interstitial loaded")
                    interstitialAd = ad
                    // Set full screen content callback to know when user closes ad
                    interstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d("AdMob", "Interstitial dismissed")
                                // After ad dismissed we can navigate if navigation was pending
                                performNavigationIfPending()
                                // Optionally preload next interstitial
                                interstitialAd = null
                                loadInterstitial()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                Log.w("AdMob", "Failed to show interstitial: $adError")
                                interstitialAd = null
                                performNavigationIfPending()
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.d("AdMob", "Interstitial showed")
                            }
                        }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.w("AdMob", "Interstitial failed to load: ${loadAdError.message}")
                    interstitialAd = null
                }
            }
        )
    }

    private var pendingNavigationBundle: Bundle? = null
    private fun performNavigationIfPending() {
        pendingNavigationBundle?.let { bundle ->
            // navigate and clear pending
            findNavController().navigate(
                R.id.videoFragment_to_tagsFragment,
                bundle
            )
            pendingNavigationBundle = null
        }
    }

    fun processdata(data: TodayVideo) {

        if(data.items.isNotEmpty()){
            binding.noData.isVisible = false
            binding.hrRecycler.isVisible = true
            recycler = binding.hrRecycler

            if (recycler.layoutManager == null) {
                recycler.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

            if (!::todayAdapter.isInitialized) {
                recycler.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                todayAdapter = TodayVideoAdaptor(requireContext(), data) { videoId ->
                    navigateToSubtagsFragment(videoId)
                }

                recycler.adapter = todayAdapter
                attachSnapHelper(recycler)
            } else {
                todayAdapter.updateData(data)
                recycler.adapter = todayAdapter
            }
            if (!decorationAdded) {
                recycler.addItemDecoration(HorizontalSpacingItemDecoration(50))
                decorationAdded = true
            }
        }
        else{
            binding.noData.isVisible = true
            binding.hrRecycler.isVisible = false
        }


    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.searchBtn.id -> {
                val value = binding.searchEditText.text.toString().trim()

                when {
                    value.isEmpty() -> {
                        binding.errorText.text = getString(R.string.please_enter_the_url)
                        binding.errorText.visibility = View.VISIBLE
                    }

                    !IsValidUrl(value) -> {
                        binding.errorText.text = getString(R.string.please_enter_the_valid_url)
                        binding.errorText.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.errorText.visibility = View.GONE

                        val bundle = Bundle().apply {
                            putString("video_url", value)
                        }

                        findNavController().navigate(
                            R.id.videoFragment_to_tagsFragment,
                            bundle
                        )
                        Log.d(
                            "FragmentNavigation",
                            "VideoDetail Fragment: Navigated to TagFragment"
                        )
                    }
                }
            }

            binding.gotoYoutube.id -> {
                // Open the YouTube app or URL
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = AppConst.youtube.toUri() // YouTube URL
                    `package` = AppConst.youtube_pakage // Open in YouTube app if available
                }

                // Check if the YouTube app is available
                if (intent.resolveActivity(requireContext().packageManager) != null) {
                    startActivity(intent)
                } else {
                    // Fallback to opening in a browser
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, AppConst.youtube.toUri())
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

        if (interstitialAd != null) {
            // set pending navigation so it happens after ad dismissed
            pendingNavigationBundle = bundle
            interstitialAd?.show(requireActivity())
        } else {
            val navController = findNavController()
            navController.navigate(R.id.videoFragment_to_tagsFragment, bundle)

            loadInterstitial()
        }

    }

    override fun onResume() {
        super.onResume()
        binding.searchEditText.text?.clear()
        if (::todayAdapter.isInitialized) {
            todayAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ProgressDialog.dismiss()
        binding.hrRecycler.adapter = null
    }
}

