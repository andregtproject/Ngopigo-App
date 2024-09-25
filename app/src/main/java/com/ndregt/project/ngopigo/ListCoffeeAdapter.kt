package com.ndregt.project.ngopigo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ListCoffeeAdapter(private val listCoffee: ArrayList<Coffee>) : RecyclerView.Adapter<ListCoffeeAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_coffee, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val coffee = listCoffee[position]
        holder.tvCategory.text = coffee.category
        holder.imgPhoto.setImageResource(coffee.photo)
        holder.tvName.text = coffee.name
        holder.tvDescription.text = coffee.description

        val context = holder.itemView.context
        
        when (coffee.category) {
            context.getString(R.string.hot_category) -> {
                holder.tvCategory.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_orange_light))
                holder.imgItemCategory.setImageResource(R.drawable.ic_hot)
                holder.imgItemCategory.setColorFilter(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_orange_light))
            }
            context.getString(R.string.cold_category) -> {
                holder.tvCategory.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_blue_bright))
                holder.imgItemCategory.setImageResource(R.drawable.ic_cold)
                holder.imgItemCategory.setColorFilter(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_blue_bright))
            }
        }

        holder.itemView.setOnClickListener { onItemClickCallback.onDetailClicked(coffee) }
    }

    override fun getItemCount(): Int = listCoffee.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
        val tvCategory: TextView = itemView.findViewById(R.id.tv_item_category)
        val imgItemCategory: ImageView = itemView.findViewById(R.id.img_item_category)
    }

    interface OnItemClickCallback {
        fun onDetailClicked(data: Coffee)
    }
}