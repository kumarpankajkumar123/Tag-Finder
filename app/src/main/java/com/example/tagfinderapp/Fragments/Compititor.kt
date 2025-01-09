package com.example.tagfinderapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tagfinderapp.R
import com.example.tagfinderapp.databinding.FragmentCompititorBinding
import com.example.tagfinderapp.databinding.FragmentDataKeyWordsBinding


class Compititor : Fragment() {

    lateinit var binding: FragmentCompititorBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompititorBinding.inflate(inflater, container, false)
        return binding.root
    }
}