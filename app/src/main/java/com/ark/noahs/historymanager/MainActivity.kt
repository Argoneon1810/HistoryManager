package com.ark.noahs.historymanager

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    lateinit var contentBody : ConstraintLayout
    lateinit var mRecyclerView : RecyclerView
    lateinit var fab : FloatingActionButton
    lateinit var mainTxt : TextView
    lateinit var icon : ImageView
    lateinit var chkbox : CheckBox
    var introduced : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contentBody = findViewById(R.id.ContentBody)
        mRecyclerView = findViewById(R.id.recView)
        fab = findViewById(R.id.BigFAB)
        mainTxt = findViewById(R.id.txt_intro)
        icon = findViewById(R.id.mainIcon)
        chkbox = findViewById(R.id.tutChkBox)

        var dataList : ArrayList<MainRecyclerData> = arrayListOf()

        val mAdapter = MainRecyclerAdapter(this, dataList)
        mRecyclerView.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        mRecyclerView.layoutManager = lm
        mRecyclerView.setHasFixedSize(true)

        fab.setOnClickListener {
            dataList.add(MainRecyclerData("","Hello","World"))    
            if(mRecyclerView.childCount > 0) setScreen_MoreThanZero()
        }
    }

    fun onClickIntro(view: View?) {
        if (!introduced){
            if(mRecyclerView.childCount > 0) {
                setScreen_MoreThanZero()
                fab.visibility = View.VISIBLE
                icon.visibility = View.GONE
                chkbox.visibility = View.GONE
                introduced = true
            } else {
                setScreen_Zero()
            }
        }
    }

    fun setScreen_MoreThanZero() {
        val displayMetrics = DisplayMetrics()
        var height: Int = displayMetrics.heightPixels + getNavigationBarHeight()
        mRecyclerView.updateLayoutParams {
            height = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                height - 64f,
                resources.displayMetrics
            ).toInt()
        }
    }

    fun setScreen_Zero() {
        mRecyclerView.updateLayoutParams {
            height = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                64f,
                resources.displayMetrics
            ).toInt()
        }
        fab.visibility = View.VISIBLE
        mainTxt.text = getString(R.string.introguide2)
        icon.alpha = .5f
        chkbox.visibility = View.GONE
        introduced = true
    }

    private fun getNavigationBarHeight(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            val usableHeight = metrics.heightPixels
            windowManager.defaultDisplay.getRealMetrics(metrics)
            val realHeight = metrics.heightPixels
            return if (realHeight > usableHeight) realHeight - usableHeight else 0
        }
        return 0
    }
}