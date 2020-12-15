package com.ark.noahs.historymanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainRecyclerAdapter (
    val context : Context,
    val itemList : ArrayList<MainRecyclerData>
) : RecyclerView.Adapter<MainRecyclerAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycleritem, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder?.bind(itemList[position], context)
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class CustomViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.itemImage)
        val title = itemView.findViewById<TextView>(R.id.itemTitle)
        val desc = itemView.findViewById<TextView>(R.id.itemDesc)

        fun bind(data: MainRecyclerData, context: Context) {
            if (data.getDataImage() != "") {
                val resID = context.resources.getIdentifier(data.getDataImage(), "Drawable", context.packageName)
                image?.setImageResource(resID)
            }
            title?.text = data.getDataTitle()
            desc?.text = data.getDataDesc()
        }
    }
}