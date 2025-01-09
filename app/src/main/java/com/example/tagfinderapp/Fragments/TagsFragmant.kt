package com.example.tagfinderapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tagfinderapp.Inteface.ApiInterface
import com.example.tagfinderapp.Model.VideoModel
import com.example.tagfinderapp.Network.RetrofitInstanse
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Repository.Repository
import com.example.tagfinderapp.Util.UserDatabase
import com.example.tagfinderapp.ViewModal.TagViewModelFactory
import com.example.tagfinderapp.ViewModal.TagsViewModel
import com.example.tagfinderapp.databinding.FragmentTagsFragmantBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class TagsFragmant : Fragment() {
    private var binding: FragmentTagsFragmantBinding? = null
    private lateinit var tagviewmodel: TagsViewModel
    lateinit var videoUrl: String
    lateinit var videoId: String
    private var isAnyChecked = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTagsFragmantBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoUrl = arguments?.getString("video_url").toString()
        val bool = arguments?.getString("boolean").toString()

        UserDatabase.init(requireContext())
        val retrofit = RetrofitInstanse
        val repository = Repository(retrofit.getRetrofit().create(ApiInterface::class.java))
        val factory = TagViewModelFactory(repository)
        tagviewmodel = ViewModelProvider(this, factory).get(TagsViewModel::class.java)
//        val videoId = receivedString?.let { getVideoIdFromUrl(it) }

        if (videoUrl != null && !bool.equals("true")) {
             videoId = getVideoIdFromUrl(videoUrl).toString()
            Log.e("videoId videourl", "" + videoId)
            load_data(videoId)
        }
        else{
            videoId = arguments?.getString("todayVideoId").toString()
            Log.e("imageId", "" + videoId)
            loadData(videoId)
        }

        if (savedInstanceState == null) {
            val subTagsFragment = SubTags()
            val bundle = Bundle()

            Log.e("TagsFragment subtags open ", "" + videoId)
            Log.e("TagsFragment subtags open ", "" + videoUrl)

//            bundle.putString("video_url", videoUrl)
            bundle.putString("videoId", videoId)
            subTagsFragment.arguments = bundle

            // Load the first subchild fragment by default
            childFragmentManager.beginTransaction()
                .replace(R.id.subchildFramlayout, subTagsFragment)
                .commit()
        }
        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedFragment: Fragment = when (tab?.position) {
                    0 -> SubTags()
                    1 -> Thumbnail()
                    2 -> MoreVideoDetails()
                    else -> {
                        Toast.makeText(
                            requireContext(),
                            "Please select a valid tab",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                }
                val bundle = Bundle()
//                bundle.putString("video_url", videoUrl)
                bundle.putString("videoId", videoId)
                selectedFragment.arguments = bundle

                // Replace the fragment in the container
                childFragmentManager.beginTransaction()
                    .replace(R.id.subchildFramlayout, selectedFragment)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle if needed
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle if needed
            }
        })

        binding?.backarrow?.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.viewpager2,
                VideoDetail()
            ) // R.id.viewpager2 is the container in MainActivity
//            transaction.addToBackStack(null) // Add to back stack if you want to maintain navigation history
            transaction.commit()
        }
        binding?.cancelall?.setOnClickListener {
            isAnyChecked = false
            // Reset the UI
            binding?.keyword1?.visibility = View.VISIBLE
            binding?.content?.visibility = View.GONE

            // Notify SubTags fragment to clear selection
            val subTagsFragment =
                childFragmentManager.findFragmentById(R.id.subchildFramlayout) as? SubTags
            subTagsFragment?.clearAllCheckboxes()
        }
        // Handle Copy Selected Tags Button
        binding?.searchbtn?.setOnClickListener {
            (childFragmentManager.findFragmentById(R.id.subchildFramlayout) as? SubTags)?.copySelectedTags()
        }

        // Handle Select All Button
        binding?.selecall?.setOnClickListener {
            (childFragmentManager.findFragmentById(R.id.subchildFramlayout) as? SubTags)?.selectAllTags()
        }

        // Handle Copy All Button
        binding?.copyall?.setOnClickListener {
            (childFragmentManager.findFragmentById(R.id.subchildFramlayout) as? SubTags)?.copyAllTags()
        }

    }

    private fun processdatatag(it: VideoModel) {
        val tittle = it.items.get(0).snippet.title

        Log.e("tittle", "" + tittle)
        binding?.keyword?.text = tittle

        val userId = UserDatabase.generateOrCreatedId()
        Log.e("TagsFragmentId", "" + userId)
        Log.e("TagFragmentData", "" + UserDatabase.getUserData(userId))

        val thumbnail = mutableListOf<String>()
        thumbnail.add(it.items.get(0).snippet.thumbnails.standard.url)
        UserDatabase.addThumbnailsForUrl(videoUrl, thumbnail, userId)
        Log.e("TagFragmentUserData", "" + UserDatabase.getUserData(userId))
    }

    private fun getVideoIdFromUrl(getvalue: String): String? {
        Log.e("getid methods", ":= call hua")
        val regex =
            "(?:youtube\\.com\\/.*[?&]v=|youtu\\.be\\/|youtube\\.com\\/shorts\\/)?([\\w\\-]{11})"

        val pattern1 = Pattern.compile(regex)
        val macher1 = pattern1.matcher(getvalue)

        if (macher1.find()) {
            return macher1.group(1)
        } else {
            Toast.makeText(requireContext(), "please enter correct video url", Toast.LENGTH_SHORT)
            return null
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    fun load_data(videoId: String) {
        Log.e("load_data", "call hua")


        lifecycleScope.launch {
            tagviewmodel.getData(
                "snippet",
                videoId,
                "AIzaSyCrdo85_ezkyP0tMC-rC52Hlpr2qPjD7E8"
            )
            tagviewmodel.tags.observe(viewLifecycleOwner, Observer {
                it.onSuccess {
                    processdatatag(it)
                }
                it.onFailure {
                    Toast.makeText(requireContext(), "on failure methods", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }
    fun loadData(videoId: String) {
        Log.e("loadData", "call hua")
        lifecycleScope.launch {
            tagviewmodel.getData(
                "snippet",
                videoId,
                "AIzaSyCrdo85_ezkyP0tMC-rC52Hlpr2qPjD7E8"
            )
            tagviewmodel.tags.observe(viewLifecycleOwner, Observer {
                it.onSuccess {
                    processdatatag(it)
                }
                it.onFailure {
                    Toast.makeText(requireContext(), "on failure methods", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }

    fun onSubTagsCheckBoxStateChanged(isAnyChecked: Boolean) {
        // Hide or show LinearLayout
        binding?.keyword1?.visibility = if (isAnyChecked) View.GONE else View.VISIBLE
        binding?.content?.visibility = if (isAnyChecked) View.VISIBLE else View.GONE

    }
}
