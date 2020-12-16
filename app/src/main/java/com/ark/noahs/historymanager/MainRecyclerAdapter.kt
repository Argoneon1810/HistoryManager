package com.ark.noahs.historymanager

import android.content.Context
import android.view.*
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MainRecyclerAdapter(
    val context: Context,
    val myInterface: MyInterface,
    val itemList: ArrayList<MainRecyclerData>
) : RecyclerView.Adapter<MainRecyclerAdapter.CustomViewHolder>() {

    interface MyInterface {
        fun notifyChange(position: Int)
    }

    inner class CustomViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        val image = itemView.findViewById<ImageView>(R.id.itemImage)
        val title = itemView.findViewById<TextView>(R.id.itemTitle)
        val desc = itemView.findViewById<TextView>(R.id.itemDesc)

        init {
            itemView.setOnCreateContextMenuListener(this);
        }

        fun bind(data: MainRecyclerData, context: Context) {
            if (data.getDataImage() != "") {
                val resID = context.resources.getIdentifier(
                    data.getDataImage(),
                    "Drawable",
                    context.packageName
                )
                image?.setImageResource(resID)
            }
            title?.text = data.getDataTitle()
            desc?.text = data.getDataDesc()
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val Delete: MenuItem? = menu?.add(Menu.NONE, 1001, 1, "삭제")
            Delete?.setOnMenuItemClickListener(onEditMenu)
        }

        private val onEditMenu: MenuItem.OnMenuItemClickListener =
            MenuItem.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    1001 -> {
                        myInterface.notifyChange(adapterPosition)
                    }
                }
                true
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder?.bind(itemList[position], context)
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    fun removeAt(position: Int) {
        itemList.removeAt(position)
    }
}