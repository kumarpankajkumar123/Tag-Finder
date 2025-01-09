package com.example.tagfinderapp.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Util.UserDatabase
import com.example.tagfinderapp.databinding.FragmentKeywordSBinding
import com.example.tagfinderapp.databinding.FragmentVideoDetailBinding

class KeywordS : Fragment(),OnClickListener {
    private var binding: FragmentKeywordSBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKeywordSBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.searchbtn?.setOnClickListener(this)
        UserDatabase.init(requireContext())
    }

    override fun onClick(view: View?) {
       when(view?.id){
           binding?.searchbtn?.id -> {
               val keyword = binding?.searchEditText?.text.toString().trim()
               if(keyword.isEmpty()){
                   Toast.makeText(requireContext(),"please enter keywords",Toast.LENGTH_SHORT).show()
               }

               else if(keyword.contains('/')){
                   Toast.makeText(requireContext(),"please enter right keywords",Toast.LENGTH_SHORT).show()
               }
               else{
                   Toast.makeText(requireContext(),"you entered something",Toast.LENGTH_SHORT).show()
                   val userId = UserDatabase.generateOrCreatedId()
                   Log.e("keywords fragmentId",""+userId)
                   UserDatabase.addKeyword(keyword,userId)
                   Log.e("keywords fragmentData",""+UserDatabase.getUserData(userId))
                   val childFragment = SubKeywords()
                   val bundle = Bundle()
                   bundle.putString("keywords", keyword) // Pass the string
                   childFragment.arguments = bundle // Set arguments
                   Log.e("fragment keyword", "" + keyword)
                   binding?.searchLinear?.visibility = GONE
                   childFragmentManager.beginTransaction()
                       .replace(R.id.insideframlayout, childFragment)
                       .addToBackStack(null)
                       .commit()
               }
           }
       }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}