package com.example.tagfinderapp.Fragments
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tagfinderapp.R
import com.example.tagfinderapp.databinding.FragmentSubKeywordsBinding
import com.google.android.material.tabs.TabLayout


class SubKeywords : Fragment() {

    lateinit var binding: FragmentSubKeywordsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubKeywordsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receivedString = arguments?.getString("keywords")
        Log.e("sub fragment keyword",""+receivedString)
        binding.keyword.text = receivedString

        binding.backarrow.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.viewpager2,
                KeywordS()
            ) // R.id.viewpager2 is the container in MainActivity
//            transaction.addToBackStack(null) // Add to back stack if you want to maintain navigation history
            transaction.commit()
        }
        if (savedInstanceState == null) {

            val subKeywords = DataKeyWords()
            val bundle = Bundle()
            bundle.putString("keywords", receivedString)
            subKeywords.arguments = bundle

            // Load the first subchild fragment by default
            childFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, subKeywords)
                .commit()
        }

        binding.tabLayout2.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedFragment: Fragment = when (tab?.position) {
                    0 -> DataKeyWords()
                    1 -> Compititor()
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
                bundle.putString("keywords", receivedString)
                selectedFragment.arguments = bundle

                // Replace the fragment in the container
                childFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, selectedFragment)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle if needed
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle if needed
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun processdatatag(list: List<*>?){

        if (list != null) {
            for (list1 in list){
                Log.e("string",""+list1)
            }
        }
    }

}