package com.example.tagfinderapp.Adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tagfinderapp.R

class KeywordsAdaptor(val context:Context, val list : List<*>) :
    RecyclerView.Adapter<KeywordsAdaptor.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.tags_design,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.text1.text = item.toString()
        holder.checbox.visibility = View.GONE
    }

    override fun getItemCount(): Int {
     return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checbox : CheckBox = itemView.findViewById<CheckBox?>(R.id.checkbox)
        val text1 : TextView = itemView.findViewById(R.id.tag_text)
    }

}