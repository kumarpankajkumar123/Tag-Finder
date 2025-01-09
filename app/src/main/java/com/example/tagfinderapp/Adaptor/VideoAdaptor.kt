package com.example.tagfinderapp.Adaptor

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.Model.VideoModel
import com.example.tagfinderapp.R

class VideoAdaptor(
    val context: Context,
    val taglist: List<String>,
    private val onCheckBoxStateChanged: (Boolean) -> Unit
) :
    RecyclerView.Adapter<VideoAdaptor.MyViewHolder>() {


        private val selectedTags = mutableSetOf<String>()
    private val checkedStates = mutableMapOf<Int, Boolean>() // Track checked positions

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.tags_design, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tag = taglist[position]
        holder.text1.text = tag

        // Reset the listener to avoid triggering it during binding
        holder.checbox.setOnCheckedChangeListener(null)

        // Set the checkbox state based on the map
        val isChecked = checkedStates[position] ?: false
        holder.checbox.isChecked = isChecked

        // Update the checkbox state and the selected tags set when checkbox is clicked
        holder.checbox.setOnCheckedChangeListener { _, isChecked ->
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
        return taglist.size
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

    fun selectAllTags() {
        for (i in taglist.indices) {
            checkedStates[i] = true
            selectedTags.add(taglist[i])
        }
        notifyDataSetChanged()
        onCheckBoxStateChanged(true) // Notify that some checkboxes are checked
        Toast.makeText(context, "All tags selected!", Toast.LENGTH_SHORT).show()
    }

    fun copyAllTags() {
        val allTags = taglist.joinToString(", ")
        copyToClipboard("All Tags", allTags)
        for (i in taglist.indices) {
            checkedStates[i] = true
            selectedTags.add(taglist[i])
        }
        notifyDataSetChanged()
        Toast.makeText(context, "All tags copied!", Toast.LENGTH_SHORT).show()
        onCheckBoxStateChanged(true) // Notify that some checkboxes are checked
    }
    private fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
    }

    fun clearSelection() {
        selectedTags.clear()
        checkedStates.clear()
        notifyDataSetChanged()
        Toast.makeText(context, "All selections cleared!", Toast.LENGTH_SHORT).show()
        onCheckBoxStateChanged(false) // Notify that all are unchecked
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checbox: CheckBox = itemView.findViewById(R.id.checkbox)
        val text1: TextView = itemView.findViewById(R.id.tag_text)
    }
}