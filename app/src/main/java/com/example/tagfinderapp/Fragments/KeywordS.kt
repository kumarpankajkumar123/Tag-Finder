package com.example.tagfinderapp.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Util.UserDatabase
import com.example.tagfinderapp.appConst.AppConst
import com.example.tagfinderapp.databinding.FragmentKeywordSBinding
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
import java.util.Locale

class KeywordS : Fragment(), OnClickListener {
    lateinit var binding: FragmentKeywordSBinding
    private var interstitialAd : InterstitialAd? = null
    private val interstitialTestUnitId = AppConst.InterstialUnitId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(requireContext()) {}
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKeywordSBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBtn.setOnClickListener(this)

        binding.searchEditText.addTextChangedListener{
            if(binding.errorText.isVisible){
                binding.errorText.isVisible = false
            }
        }
        binding.voiceImg.setOnClickListener {
            startSpeechToText()
        }
        loadInterstitial()
        setupBanner()
    }

    private fun startSpeechToText() {
        if (hasAudioPermission()) {
            startSpeechToTextInternal()
        } else {
            audioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun startSpeechToTextInternal() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak keyword")
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,requireContext().packageName)
        }

        try {
            speechLauncher.launch(intent)
        } catch (e: Exception) {
            binding.errorText.apply {
                isVisible = true
                text = getString(R.string.please_enter_the_speech)
            }
        }
    }
    private val speechLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("VOICE", "ResultCode = ${result.resultCode}")
            Log.d("VOICE", "Data = ${result.data}")
            if (result.resultCode == Activity.RESULT_OK) {
                val spokenText = result.data
                    ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?.firstOrNull()

                spokenText?.let {
                    binding.searchEditText.setText(it)
                    binding.searchEditText.setSelection(it.length) // cursor end me
                    Log.d("VOICE", "Text = ${spokenText}")
                }
            }
        }

    private val audioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startSpeechToTextInternal()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Microphone permission is required for voice search",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }



    private fun hasAudioPermission(): Boolean {
        return requireContext().checkSelfPermission(
            android.Manifest.permission.RECORD_AUDIO
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
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
                    interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
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
                R.id.keywordsFragment_to_subKeywordsFragment,
                bundle
            )
            pendingNavigationBundle = null
        }
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            binding.searchBtn.id -> {
                val keyword = binding.searchEditText.text.toString().trim()

                if (keyword.isEmpty()) {
                    binding.errorText.text = getString(R.string.please_enter_the_keywords)
                    binding.errorText.visibility = View.VISIBLE
                    return
                }
                if (keyword.contains('/')) {
                    binding.errorText.text = getString(R.string.please_enter_the_valid_keywords)
                    binding.errorText.visibility = View.VISIBLE
                    return
                }

                binding.searchEditText.text?.clear()
                val bundle = Bundle().apply { putString("keywords", keyword) }
                // If ad is ready, show it and navigate in callback; else navigate immediately
                if (interstitialAd != null) {
                    // set pending navigation so it happens after ad dismissed
                    pendingNavigationBundle = bundle
                    interstitialAd?.show(requireActivity())
                } else {
                    // fallback: no ad loaded, navigate immediately
                    findNavController().navigate(R.id.keywordsFragment_to_subKeywordsFragment, bundle)
                    // Also try loading an interstitial for next time
                    loadInterstitial()
                }
            }
        }
    }

    private fun setupBanner() {

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
    }

    override fun onDestroyView() {
        // clean references
        interstitialAd = null
        pendingNavigationBundle = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
    }
}