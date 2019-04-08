package com.taijuan.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pools
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.github.chrisbanes.photoview.PhotoView
import com.taijuan.ImagePicker
import com.taijuan.data.ImageItem

internal class ImagePageAdapter(
        private val mActivity: Activity,
        private val images: MutableList<ImageItem> = mutableListOf()
) : androidx.viewpager.widget.PagerAdapter() {
    private val pool: Pools.SynchronizedPool<PhotoView> = Pools.SynchronizedPool(5)
    private var listener: OnPhotoTapListener? = null
    fun setOnPhotoTapListener(listener: OnPhotoTapListener?) {
        this.listener = listener
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var photoView = pool.acquire()
        photoView = photoView ?: PhotoView(mActivity.application)
        val imageItem = images[position]
        ImagePicker.imageLoader.displayImagePreview(mActivity, imageItem.path, photoView, photoView.measuredWidth, photoView.measuredHeight)
        photoView.setOnPhotoTapListener(listener)
        container.addView(photoView)
        return photoView
    }

    override fun getCount(): Int = images.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        val photoView = any as PhotoView
        container.removeView(photoView)
        pool.release(photoView)
    }
}