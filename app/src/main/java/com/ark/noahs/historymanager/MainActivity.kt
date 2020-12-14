package com.ark.noahs.historymanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    lateinit var contentBody : ConstraintLayout
    var introduced : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contentBody = findViewById(R.id.ContentBody)
    }

    fun onClickIntro(view: View?) {
        if (!introduced){
            contentBody.layoutParams.height = 0
            introduced = true
        }
    }
}