package com.example.tagfinderapp.Adaptor

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tagfinderapp.Fragments.MoreVideoDetails
import com.example.tagfinderapp.Fragments.SubTags
import com.example.tagfinderapp.Fragments.Thumbnail
import com.example.tagfinderapp.Model.VideoModel

class SubChildPagerAdapter(
    fragment: Fragment,
    private val todayVideo: VideoModel
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        val fragment = when (position) {
            0 -> SubTags(todayVideo)
            1 -> Thumbnail(todayVideo)
            2 -> MoreVideoDetails(todayVideo)
            else -> SubTags(todayVideo)
        }
        return fragment
    }
}
