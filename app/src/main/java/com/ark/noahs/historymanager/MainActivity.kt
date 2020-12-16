package com.ark.noahs.historymanager

import android.content.Context
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.ark.noahs.historymanager.databinding.ActivityMainBinding
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), MainRecyclerAdapter.MyInterface {
    private lateinit var binding: ActivityMainBinding
    var introduced : Boolean = false
    var state : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Recycler View Setup
        var dataList : ArrayList<MainRecyclerData> = arrayListOf()
        val mAdapter = MainRecyclerAdapter(this, this, dataList)
        binding.recView.adapter = mAdapter
        val lm = LinearLayoutManager(this)
        binding.recView.layoutManager = lm
        binding.recView.setHasFixedSize(true)

        binding.recView.addOnItemTouchListener(
            RecyclerTouchListener(
                applicationContext,
                binding.recView,
                object : ClickListener {
                    override fun onClick(view: View?, position: Int) {}

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )

        //FAB onClick
        binding.BigFAB.setOnClickListener {
            dataList.add(MainRecyclerData("", "Hello", "World"))
            mAdapter.notifyItemInserted(mAdapter.itemCount - 1)
           if(state == 1 && 0 < binding.recView.adapter?.itemCount ?: 0) {
               binding.root.transitionToState(R.id.nonzero_root)
               binding.Intro.transitionToState(R.id.nonzero_intro)
               state = 2
           }
        }
    }

    fun onClickIntro(view: View?) {
        if (!introduced){
            if(0 < binding.recView.adapter?.itemCount ?: 0) {
                binding.root.transitionToState(R.id.nonzero_root)
                binding.Intro.transitionToState(R.id.nonzero_intro)
                state = 2
            } else {
                binding.root.transitionToState(R.id.zero_root)
                binding.Intro.transitionToState(R.id.zero_intro)
                binding.txtIntro.text = getString(R.string.introguide2)
                state = 1
            }
        }
    }

    override fun notifyChange(position: Int) {
        val adapter = binding.recView.adapter
        (adapter as MainRecyclerAdapter).removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyDataSetChanged()
        if(0 == binding.recView.adapter?.itemCount ?: 0) {
            binding.root.transitionToState(R.id.zero_root)
            binding.Intro.transitionToState(R.id.zero_intro)
            state = 1
        }
    }

    interface ClickListener {
        fun onClick(view: View?, position: Int)
        fun onLongClick(view: View?, position: Int)
    }

    class RecyclerTouchListener(
        context: Context?,
        recyclerView: RecyclerView,
        clickListener: ClickListener?
    ) : OnItemTouchListener {
        private val gestureDetector: GestureDetector
        private val clickListener: ClickListener? = clickListener

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

        init {
            gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(
                            child,
                            recyclerView.getChildAdapterPosition(child)
                        )
                    }
                }
            })
        }
    }
}