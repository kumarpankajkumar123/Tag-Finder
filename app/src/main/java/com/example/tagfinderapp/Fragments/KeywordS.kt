package com.example.tagfinderapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Util.UserDatabase
import com.example.tagfinderapp.databinding.FragmentKeywordSBinding

class KeywordS : Fragment(), OnClickListener {
    lateinit var binding: FragmentKeywordSBinding

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

        binding.searchbtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.searchbtn.id -> {
                val keyword = binding.searchEditText.text.toString().trim()
                if (keyword.isEmpty()) {
                    Toast.makeText(requireContext(), "please enter keywords", Toast.LENGTH_SHORT)
                        .show()
                } else if (keyword.contains('/')) {
                    Toast.makeText(
                        requireContext(),
                        "please enter right keywords",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d("keywords", "onClick: ${keyword}")
                    val bundle = Bundle()
                    bundle.putString("keywords", keyword)
                    findNavController().navigate(
                        R.id.keywordsFragment_to_subKeywordsFragment,
                        bundle
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.searchEditText.text?.clear()
    }
}