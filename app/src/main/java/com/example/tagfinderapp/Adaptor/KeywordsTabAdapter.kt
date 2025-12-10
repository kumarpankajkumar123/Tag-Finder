package com.example.tagfinderapp.Adaptor


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tagfinderapp.Fragments.Competitors
import com.example.tagfinderapp.Fragments.DataKeyWords

class KeywordsTabAdapter(
    fragmant: Fragment,
    private val keyword: String
) : FragmentStateAdapter(fragmant) {
    override fun createFragment(position: Int): Fragment {

        val fragment = when (position) {
            0 -> DataKeyWords(keyword)
            1 -> Competitors(keyword)
            else -> DataKeyWords(keyword = keyword)
        }
        return fragment
    }

    override fun getItemCount(): Int = 2

}