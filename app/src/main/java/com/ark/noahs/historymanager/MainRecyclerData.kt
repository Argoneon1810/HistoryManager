package com.ark.noahs.historymanager

class MainRecyclerData(image: String, title: String, desc: String) {
    private var image : String
    private var title : String
    private var desc : String

    init {
        this.image = image
        this.title = title
        this.desc = desc
    }

    fun getDataImage() : String { return image }
    fun getDataTitle() : String { return title }
    fun getDataDesc() : String { return desc }
    fun setDataImage(image: String) { this.image = image }
    fun setDataTitle(title: String) { this.title = title }
    fun setDataDesc(desc: String) { this.desc = desc }
}