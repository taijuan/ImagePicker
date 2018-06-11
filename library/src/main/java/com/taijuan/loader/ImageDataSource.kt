package com.taijuan.loader

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.util.Log
import com.taijuan.data.ImageFolder

internal class ImageDataSource(private val activity: FragmentActivity) : LoaderManager.LoaderCallbacks<ArrayList<ImageFolder>> {
    private lateinit var loadedListener: OnImagesLoadedListener                 //图片加载完成的回调接口

    init {
        activity.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                activity.supportLoaderManager.destroyLoader(1)
                activity.lifecycle.removeObserver(this)
            }
        })
        activity.supportLoaderManager.initLoader(1, null, this)
    }

    fun loadImage(loadedListener: OnImagesLoadedListener) {
        this.loadedListener = loadedListener
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<ImageFolder>> = ImageDataLoader(activity, activity.lifecycle)

    override fun onLoadFinished(loader: Loader<ArrayList<ImageFolder>>, data: ArrayList<ImageFolder>?) {
        loadedListener.onImagesLoaded(data ?: arrayListOf())
    }

    override fun onLoaderReset(loader: Loader<ArrayList<ImageFolder>>) {
        Log.e("zuiweng", "onLoaderReset")
    }


    /**
     * 所有图片加载完成的回调接口
     */
    interface OnImagesLoadedListener {
        fun onImagesLoaded(imageFolders: ArrayList<ImageFolder>)
    }
}