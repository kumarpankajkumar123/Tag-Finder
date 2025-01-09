package com.example.tagfinderapp.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Util.UserDatabase
import com.example.tagfinderapp.databinding.FragmentHistoryBinding
import com.google.android.material.tabs.TabLayout

class History : Fragment() {
    lateinit var _binding : FragmentHistoryBinding
    lateinit var shared : SharedPreferences
    lateinit var userId : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater,container,false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shared = requireContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        userId = shared.getString("userId",null).toString()

        UserDatabase.init(requireContext())
        val userId = UserDatabase.generateOrCreatedId()
        Log.e("HistoryFragmentId",""+userId)
        Log.e("HistoryFragmentData",""+UserDatabase.getUserData(userId))

        if (savedInstanceState == null) {
            val historyTag = HistoryTags()
            // Load the first subchild fragment by default
            childFragmentManager.beginTransaction()
                .replace(R.id.historyframlayout, historyTag)
                .commit()
        }

        _binding.tabLayout3.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedFragment :Fragment = when (tab?.position){
                    0 -> HistoryTags()
                    1 -> HistoryKeywords()
                    else -> { Toast.makeText(requireContext(),"please select tab",Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                childFragmentManager.beginTransaction()
                    .replace(R.id.historyframlayout, selectedFragment)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}