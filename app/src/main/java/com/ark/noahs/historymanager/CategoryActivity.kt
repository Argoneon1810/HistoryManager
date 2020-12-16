package com.ark.noahs.historymanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ark.noahs.historymanager.databinding.ActivityCategoryBinding


class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)

        val extras = intent.extras

        if (extras?.getString("image") != null && extras?.getString("image") != "") {
            val resID = this.resources.getIdentifier(
                extras.getString("image"),
                "Drawable",
                this.packageName
            )
            binding.catImage?.setImageResource(resID)
        }
        binding.catTitle.text = extras?.getString("title")
        binding.catDesc.text = extras?.getString("desc")
    }
}