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

    interface MyInterface {                                                                         //메인 액티비티에 값 변경 고지용 인터페이스
        fun notifyChange(position: Int)
    }

    inner class CustomViewHolder(                                                                   //커스텀 뷰홀더
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        val image = itemView.findViewById<ImageView>(R.id.itemImage)
        val title = itemView.findViewById<TextView>(R.id.itemTitle)
        val desc = itemView.findViewById<TextView>(R.id.itemDesc)

        init {
            itemView.setOnCreateContextMenuListener(this);
        }

        fun bind(data: MainRecyclerData, context: Context) {                                            //딱히 특별한거 없어서 스킵
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

        override fun onCreateContextMenu(                                                               //리사이클러 아이템에 메뉴를 부착할거라는 지정
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val Delete: MenuItem? = menu?.add(Menu.NONE, 1001, 1, "삭제")                 //삭제 메뉴 하나만 등록함
            Delete?.setOnMenuItemClickListener(onEditMenu)                                                  //...
        }

        private val onEditMenu: MenuItem.OnMenuItemClickListener =                                      //메뉴 아이템용 클릭 리스너
            MenuItem.OnMenuItemClickListener { item ->
                when (item.itemId) {                                                                            //when 구문 잘 모름
                    1001 -> {                                                                                       //터치된 메뉴 아이템이 위에서 지정한 삭제 메뉴 아이디인 1001일 때
                        myInterface.notifyChange(adapterPosition)                                                       //삭제 고지 (원래 여기서 삭제시켰는데 애니메이션 작업하려다가 넘어갔음)
                    }
                }
                true
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {           //리사이클러 아이템에 레이아웃 등록하는 구문
        val view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {                        //뷰홀더 바인딩
        holder?.bind(itemList[position], context)
    }

    override fun getItemCount(): Int {                                                              //아이템 카운트 리턴
        return itemList.count()                                                                         //왜인지 모르겠는데 자꾸 0을 리턴함
    }

    fun removeAt(position: Int) {                                                                   //대상 삭제 메소드
        itemList.removeAt(position)                                                                     //58번 라인에서 말한 것
    }
}