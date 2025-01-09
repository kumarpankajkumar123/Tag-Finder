package com.example.tagfinderapp.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.Adaptor.KeywordsAdaptor
import com.example.tagfinderapp.Adaptor.TodayVideoAdaptor
import com.example.tagfinderapp.Inteface.ApiInterface
import com.example.tagfinderapp.Network.RetrofitInstanse
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Repository.Repository
import com.example.tagfinderapp.ViewModal.KeywordsViewModelFctory
import com.example.tagfinderapp.ViewModal.KeyworsViewModel
import com.example.tagfinderapp.databinding.FragmentDataKeyWordsBinding
import kotlinx.coroutines.launch

class DataKeyWords : Fragment() {
    lateinit var binding : FragmentDataKeyWordsBinding
    lateinit var keywordsViewModel: KeyworsViewModel
    lateinit var recycler: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDataKeyWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receivedString = arguments?.getString("keywords")
        Log.e("sub fragment keyword",""+receivedString)

        val retrofit = RetrofitInstanse
        // Create the Repository
        val repository = Repository(retrofit.getKeywordsRetrofit().create(ApiInterface::class.java))
        // Create the ViewModelFactory with the repository
        val factory = KeywordsViewModelFctory(repository)
        // Get the ViewModel using ViewModelProvider and the factory
        keywordsViewModel = ViewModelProvider(this, factory).get(KeyworsViewModel::class.java)

        keywordsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        lifecycleScope.launch {
            keywordsViewModel.getKeywords(receivedString.toString())
            keywordsViewModel.data.observe(viewLifecycleOwner) { result ->
                result.onSuccess { json ->
                    // Use the processed data here, for example:
                    val query = json[0] as? String
                    val suggestions = json[1] as? List<*>
                    val suggestSubtypes = (json[3] as? Map<*, *>)?.get("google:suggestsubtypes") as? List<List<Int>>
                    if (suggestions != null) {
                        processdatatag(suggestions)
                    }
                    // Update UI with this data
                    Log.d("Data Observed", "Query: $query, Suggestions: $suggestions")
                }
                result.onFailure { error ->
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun processdatatag(list : List<*>){
        recycler = binding.tagsRecyclerview
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler.adapter = KeywordsAdaptor(requireContext(), list)
    }

}