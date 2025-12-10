package com.example.tagfinderapp.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.tagfinderapp.Adaptor.HistoryViewPager2Adapter
import com.example.tagfinderapp.Adaptor.KeywordsTabAdapter
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Util.UserDatabase
import com.example.tagfinderapp.appConst.AppConst
import com.example.tagfinderapp.databinding.CustomTabLayoutBinding
import com.example.tagfinderapp.databinding.FragmentHistoryBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class History : Fragment() {
    lateinit var binding : FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UserDatabase.init(requireContext())

        val pagerAdapter = HistoryViewPager2Adapter(this)
        binding.historyViewpager2.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout3, binding.historyViewpager2) { tab, position ->
            val customView = CustomTabLayoutBinding.inflate(LayoutInflater.from(requireContext()))

            customView.tabText.text = when (position) {
                0 -> AppConst.Tags
                1 -> AppConst.Keywords
                else -> AppConst.Tags
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

        binding.tabLayout3.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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