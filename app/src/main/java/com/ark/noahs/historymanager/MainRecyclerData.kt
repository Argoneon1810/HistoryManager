package com.ark.noahs.historymanager

import android.media.Image

class MainRecyclerData(image: Image, title: String, desc: String) {
    private var image : Image
    private var title : String
    private var desc : String

    init {
        this.image = image
        this.title = title
        this.desc = desc
    }

    fun getDataImage() : Image { return image }
    fun getDataTitle() : String { return title }
    fun getDataDesc() : String { return desc }
    fun setDataImage(image: Image) { this.image = image }
    fun setDataTitle(title: String) { this.title = title }
    fun setDataDesc(desc: String) { this.desc = desc }
}