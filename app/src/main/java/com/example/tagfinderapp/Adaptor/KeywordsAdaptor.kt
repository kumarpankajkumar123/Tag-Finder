package com.example.tagfinderapp.Adaptor

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.Model.ItemXX
import com.example.tagfinderapp.databinding.TagsDesignBinding

class KeywordsAdaptor(
    val context: Context,
    val list: List<ItemXX>,
    private val onCheckBoxStateChanged: (Boolean) -> Unit
) :
    RecyclerView.Adapter<KeywordsAdaptor.MyViewHolder>() {

    private val selectedTags = mutableSetOf<String>()
    private val checkedStates = mutableMapOf<Int, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = TagsDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        val tag = item.snippet?.channelTitle?:""

        val channelItem = item.snippet?.channelTitle
        if (channelItem != null) {
            holder.binding.checkbox.text = channelItem
        }

        holder.binding.checkbox.setOnCheckedChangeListener(null)
        val isChecked = checkedStates[position] ?: false
        holder.binding.checkbox.isChecked = isChecked

        holder.binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            checkedStates[position] = isChecked
            if (isChecked) {
                selectedTags.add(tag)
            } else {
                selectedTags.remove(tag)
            }

            // Log the current state for debugging
            Log.d("VideoAdaptor", "Tag: $tag, IsChecked: $isChecked")
            Log.d("VideoAdaptor", "SelectedTags: $selectedTags")

            // Notify the parent about the state change
            onCheckBoxStateChanged(checkedStates.containsValue(true))
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(val binding: TagsDesignBinding) : RecyclerView.ViewHolder(binding.root)

    fun selectAllTags() {
        for (i in list.indices) {
            val channelTitle = list[i].snippet?.channelTitle ?: continue
            checkedStates[i] = true
            selectedTags.add(channelTitle)
        }
        notifyDataSetChanged()
        onCheckBoxStateChanged(true) // Notify that some checkboxes are checked

    }

    fun copySelectedTags() {
        val selectedTagsText = selectedTags.joinToString(", ")
        if (selectedTagsText.isNotEmpty()) {
            copyToClipboard("Selected Tags", selectedTagsText)
            Toast.makeText(context, "Selected tags copied!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No tags selected!", Toast.LENGTH_SHORT).show()
        }
    }

    fun copyAllTags() {
        val allTags = list.joinToString(", ")
        copyToClipboard("All Tags", allTags)
        for (i in list.indices) {
            val channelTitle = list[i].snippet?.channelTitle ?: continue
            checkedStates[i] = true
            selectedTags.add(channelTitle)
        }
        notifyDataSetChanged()
        onCheckBoxStateChanged(true) // Notify that some checkboxes are checked
    }

    private fun copyToClipboard(label: String, text: String) {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
    }

    fun clearSelection() {
        selectedTags.clear()
        checkedStates.clear()
        notifyDataSetChanged()
        onCheckBoxStateChanged(false) // Notify that all are unchecked
    }
}