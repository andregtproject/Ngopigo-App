package com.ndregt.project.ngopigo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AboutAdapter(private val items: List<About>) : RecyclerView.Adapter<AboutAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val text: TextView = view.findViewById(R.id.text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_about, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.icon.setImageResource(item.icon)

        Glide.with(holder.itemView.context)
            .load(item.icon)
            .into(holder.icon)

        holder.text.text = item.text
        holder.itemView.setOnClickListener { item.action() }
    }

    override fun getItemCount() = items.size
}
