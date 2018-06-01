package com.taijuan.adapter

import android.app.Activity
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.github.chrisbanes.photoview.PhotoView
import com.taijuan.ImagePicker
import com.taijuan.data.ImageItem

class ImagePageAdapter(
        private val mActivity: Activity,
        private var images: MutableList<ImageItem> = mutableListOf()
) : PagerAdapter() {

    private var listener: OnPhotoTapListener? = null
    fun setOnPhotoTapListener(listener: OnPhotoTapListener?) {
        this.listener = listener
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val photoView = PhotoView(mActivity)
        val imageItem = images[position]
        ImagePicker.imageLoader.displayImagePreview(mActivity, imageItem.path, photoView, photoView.measuredWidth, photoView.measuredHeight)
        photoView.setOnPhotoTapListener(listener)
        container.addView(photoView)
        return photoView
    }

    override fun getCount(): Int = images.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}