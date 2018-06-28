package com.taijuan

import android.app.Activity
import android.widget.ImageView

interface ImageLoader {

    fun displayImage(activity: Activity, path: String?, imageView: ImageView, width: Int, height: Int)

    fun displayImagePreview(activity: Activity, path: String?, imageView: ImageView, width: Int, height: Int)

    fun clearMemoryCache()
}