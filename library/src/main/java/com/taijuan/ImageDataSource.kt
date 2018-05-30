package com.taijuan

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.FragmentActivity
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import com.taijuan.data.ImageFolder
import com.taijuan.data.ImageItem
import com.taijuan.library.R
import java.io.File

private val IMAGE_PROJECTION = arrayOf(//查询图片需要的数据列
        MediaStore.Images.Media.DISPLAY_NAME, //图片的显示名称  aaa.jpg
        MediaStore.Images.Media.DATA, //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
        MediaStore.Images.Media.SIZE, //图片的大小，long型  132492
        MediaStore.Images.Media.WIDTH, //图片的宽度，int型  1920
        MediaStore.Images.Media.HEIGHT, //图片的高度，int型  1080
        MediaStore.Images.Media.MIME_TYPE, //图片的类型     image/jpeg
        MediaStore.Images.Media.DATE_ADDED)    //图片被添加的时间，long型  1450518608

class ImageDataSource(private val activity: FragmentActivity) : LoaderManager.LoaderCallbacks<Cursor> {
    private var loadedListener: OnImagesLoadedListener? = null                     //图片加载完成的回调接口
    private val imageFolders = arrayListOf<ImageFolder>()   //所有的图片文件夹
    private var currentMode: Int? = null

    fun loadImage(loadedListener: OnImagesLoadedListener) {
        this.loadedListener = loadedListener
        destroyLoader()
        activity.supportLoaderManager.initLoader(1, null, this)//加载所有的图片
    }

    override fun onCreateLoader(id: Int, args: Bundle?) = CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC")

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        imageFolders.clear()
        if (data != null) {
            val allImages = arrayListOf<ImageItem>()   //所有图片的集合,不分文件夹
            while (data.moveToNext()) {
                val imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
                val imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
                val imageFile = File(imagePath)
                if (!imageFile.exists() || imageFile.length() <= 0) {
                    continue
                }
                val imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))
                val imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]))
                val imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]))
                val imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]))
                val imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]))
                val imageItem = ImageItem().apply {
                    path = imagePath
                    name = imageName
                    size = imageSize
                    addTime = imageAddTime
                    height = imageHeight
                    width = imageWidth
                    mimeType = imageMimeType
                }
                allImages.add(imageItem)
                val imageFolder = ImageFolder().apply {
                    name = imageFile.parentFile.name
                    path = imageFile.parentFile.absolutePath
                }
                if (!imageFolders.contains(imageFolder)) {
                    imageFolder.images = arrayListOf()
                    imageFolder.images.add(imageItem)
                    imageFolder.cover = imageItem
                    imageFolders.add(imageFolder)
                } else {
                    imageFolders[imageFolders.indexOf(imageFolder)].images.add(imageItem)
                }
            }
            if (data.count > 0 && allImages.size > 0) {
                imageFolders.add(0, ImageFolder().apply {
                    name = activity.resources.getString(R.string.ip_all_images)
                    path = "/"
                    cover = allImages[0]
                    images = allImages
                })
            }
        }

        //回调接口，通知图片数据准备完成
        loadedListener?.onImagesLoaded(imageFolders)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        println("--------")
    }

    fun destroyLoader() {
        if (currentMode != null) {
            activity.supportLoaderManager.destroyLoader(1)
        }
    }

    /**
     * 所有图片加载完成的回调接口
     */
    interface OnImagesLoadedListener {
        fun onImagesLoaded(imageFolders: List<ImageFolder>)
    }
}