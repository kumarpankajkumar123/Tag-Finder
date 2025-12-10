package com.example.tagfinderapp.Adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.databinding.KeywordsLayoutBinding

class HistoryKeywordsAdapter(
    val context: Context,
    val list: List<String>
) : RecyclerView.Adapter<HistoryKeywordsAdapter.HistoryKeywordsClass>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryKeywordsClass {
        val binding = KeywordsLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return HistoryKeywordsClass(binding)
    }

    override fun onBindViewHolder(
        holder: HistoryKeywordsClass,
        position: Int
    ) {
        val item = list[position]
        holder.binding.historyKeywords.text = item
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class HistoryKeywordsClass(val binding: KeywordsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}