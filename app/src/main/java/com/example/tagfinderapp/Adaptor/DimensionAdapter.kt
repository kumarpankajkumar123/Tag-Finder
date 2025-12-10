package com.example.tagfinderapp.Adaptor

import android.content.Context
import android.util.Log
import android.util.MutableInt
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.Model.DimensionModel
import com.example.tagfinderapp.databinding.TagsDesignBinding

class DimensionAdapter(
    val context: Context,
    val list: List<DimensionModel>,
    private val onCheckBoxStateChanged: (Boolean, Int) -> Unit
) : RecyclerView.Adapter<DimensionAdapter.MyDimesionClass>() {

    private val selectedTags = mutableSetOf<String?>()
    private var count = 0
    private val checkedStates = mutableMapOf<Int, Boolean>() // Track checked positions

    inner class MyDimesionClass(val binging: TagsDesignBinding) :
        RecyclerView.ViewHolder(binging.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DimensionAdapter.MyDimesionClass {
        val binding = TagsDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyDimesionClass(binding)

    }

    override fun onBindViewHolder(holder: DimensionAdapter.MyDimesionClass, position: Int) {
        val item = list[position]

        val width = item.width
        val height = item.height
        holder.binging.checkbox.text = "${width} x ${height}"
        holder.binging.checkbox.setOnCheckedChangeListener(null)

        val isChecked = checkedStates[position] ?: false
        holder.binging.checkbox.isChecked = isChecked


        holder.binging.checkbox.setOnCheckedChangeListener { _, isChecked ->
            checkedStates[position] = isChecked
            if (isChecked) {
                selectedTags.add(item.imageUrl)
            } else {
                selectedTags.remove(item.imageUrl)
            }

            count = selectedTags.size

            // Log the current state for debugging
            Log.d("VideoAdaptor", "Tag: ${width} x ${height}, IsChecked: $isChecked")
            Log.d("VideoAdaptor", "SelectedTags: $selectedTags")

            // Notify the parent about the state change
            onCheckBoxStateChanged(checkedStates.containsValue(true), count)
        }

        /*holder.bind(item)*/
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun clearSelection() {
        selectedTags.clear()
        checkedStates.clear()
        count = 0
        notifyDataSetChanged()
        onCheckBoxStateChanged(false, 0) // Notify that all are unchecked
    }

    fun getSelectedUrl() : MutableSet<String?> {
        return selectedTags
    }
}