package com.example.tagfinderapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tagfinderapp.Adaptor.KeywordsTabAdapter
import com.example.tagfinderapp.R
import com.example.tagfinderapp.databinding.CustomTabLayoutBinding
import com.example.tagfinderapp.databinding.FragmentSubKeywordsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.graphics.Typeface
import com.example.tagfinderapp.Util.UserDatabase
import com.example.tagfinderapp.appConst.AppConst


class SubKeywords : Fragment() {

    lateinit var binding: FragmentSubKeywordsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSubKeywordsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receivedString = arguments?.getString("keywords")
        Log.e("sub fragment keyword", "" + receivedString)

        if (receivedString != null) {
            binding.keyword.text = receivedString
        }

        binding.backarrow.setOnClickListener {
            findNavController().navigateUp()
        }

        if (receivedString != null) {
            UserDatabase.addKeyword(receivedString)
            val pagerAdapter = KeywordsTabAdapter(this, receivedString)
            binding.keywordsViewPager2.adapter = pagerAdapter
            TabLayoutMediator(binding.tabLayout2, binding.keywordsViewPager2) { tab, position ->
                val customView = CustomTabLayoutBinding.inflate(LayoutInflater.from(requireContext()))

                customView.tabText.text = when (position) {
                    0 -> AppConst.Keywords
                    1 -> AppConst.competitors
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

            binding.tabLayout2.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
    }
}