package com.example.tagfinderapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.tagfinderapp.Adaptor.HomeOffersAdapter
import com.example.tagfinderapp.Model.ImageModel
import com.example.tagfinderapp.R
import com.example.tagfinderapp.databinding.DialogiscreenshotBinding

class AppTutorialFragment : Fragment() {

    lateinit var binding: DialogiscreenshotBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogiscreenshotBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = listOf(
            ImageModel(R.drawable.first, "Open Any Youtube Video Whose tags you want to find."),
            ImageModel(R.drawable.second, "Click on Share Button & Copy link"),
            ImageModel(R.drawable.third, "Paste in App Search Box and Get Tags Of the Video")
        )

        val adapter1 = HomeOffersAdapter(requireContext(), imageList)
        binding.photoViewpager2.adapter = adapter1

        binding.dotsIndicator.attachTo(binding.photoViewpager2)

        binding.title.text = imageList.first().title

        binding.photoViewpager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.title.text = imageList[position].title
            }
        })

        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}