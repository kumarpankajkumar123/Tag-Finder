package com.example.tagfinderapp.Adaptor

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tagfinderapp.Fragments.HistoryKeywords
import com.example.tagfinderapp.Fragments.HistoryTags

class HistoryViewPager2Adapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {

        val fragment = when (position) {
            0 -> HistoryTags()
            1 -> HistoryKeywords()
            else -> HistoryTags()
        }
        return fragment
    }

    override fun getItemCount(): Int = 2

}