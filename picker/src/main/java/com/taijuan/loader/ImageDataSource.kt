package com.taijuan.loader

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.taijuan.data.ImageFolder

internal class ImageDataSource(private val activity: androidx.fragment.app.FragmentActivity) : androidx.loader.app.LoaderManager.LoaderCallbacks<ArrayList<ImageFolder>> {
    private lateinit var loadedListener: OnImagesLoadedListener                 //图片加载完成的回调接口
    private val loaderManager by lazy { androidx.loader.app.LoaderManager.getInstance(activity) }

    init {
        activity.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                loaderManager.destroyLoader(1)
                activity.lifecycle.removeObserver(this)
            }
        })
        loaderManager.initLoader(1, null, this)
    }

    fun loadImage(loadedListener: OnImagesLoadedListener) {
        this.loadedListener = loadedListener
    }

    override fun onCreateLoader(id: Int, args: Bundle?): androidx.loader.content.Loader<ArrayList<ImageFolder>> = ImageDataLoader(activity, activity.lifecycle)

    override fun onLoadFinished(loader: androidx.loader.content.Loader<ArrayList<ImageFolder>>, data: ArrayList<ImageFolder>?) {
        loadedListener.onImagesLoaded(data ?: arrayListOf())
    }

    override fun onLoaderReset(loader: androidx.loader.content.Loader<ArrayList<ImageFolder>>) {
        Log.e("zuiweng", "onLoaderReset")
    }


    /**
     * 所有图片加载完成的回调接口
     */
    interface OnImagesLoadedListener {
        fun onImagesLoaded(imageFolders: ArrayList<ImageFolder>)
    }
}