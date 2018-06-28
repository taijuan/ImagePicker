/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taijuan.loader

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.ContentResolverCompat
import com.taijuan.ImagePicker
import com.taijuan.data.ImageFolder
import com.taijuan.data.ImageItem
import com.taijuan.picker.R
import java.io.File

private val IMAGE_PROJECTION = arrayOf(//查询图片需要的数据列
        MediaStore.MediaColumns.DISPLAY_NAME, //图片的显示名称  aaa.jpg
        MediaStore.MediaColumns.DATA, //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
        MediaStore.MediaColumns.SIZE, //图片的大小，long型  132492
        MediaStore.MediaColumns.WIDTH, //图片的宽度，int型  1920
        MediaStore.MediaColumns.HEIGHT, //图片的高度，int型  1080
        MediaStore.MediaColumns.MIME_TYPE, //图片的类型     image/jpeg
        MediaStore.MediaColumns.DATE_ADDED)    //图片被添加的时间，long型  1450518608

internal const val IMAGE_SELECTION = "${MediaStore.Files.FileColumns.MEDIA_TYPE} =${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE} AND ${MediaStore.Files.FileColumns.SIZE}>0"
internal const val VIDEO_SELECTION = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO} AND ${MediaStore.Files.FileColumns.SIZE}>0"


internal class ImageDataLoader(context: Context, lifecycle: Lifecycle) : AsyncTaskLoader<ArrayList<ImageFolder>>(context) {

    private val data = arrayListOf<ImageFolder>()
    private val observer by lazy { ForceLoadContentObserver() }

    init {
        context.applicationContext.contentResolver.registerContentObserver(MediaStore.Files.getContentUri("external"), false, observer)
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                context.contentResolver.unregisterContentObserver(observer)
                lifecycle.removeObserver(this)
            }
        })
    }

    override fun loadInBackground(): ArrayList<ImageFolder>? {
        data.clear()
        var cursor: Cursor? = null
        try {
            cursor = ContentResolverCompat.query(context.contentResolver, MediaStore.Files.getContentUri("external"), IMAGE_PROJECTION, ImagePicker.pickHelper.selection, arrayOf(), IMAGE_PROJECTION[6] + " DESC", null)
            if (cursor != null) {
                val allImages = arrayListOf<ImageItem>()   //所有图片的集合,不分文件夹
                while (cursor.moveToNext()) {
                    val imageName = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
                    val imagePath = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
                    val imageFile = File(imagePath)
                    if (!imageFile.exists() || imageFile.length() <= 0) {
                        continue
                    }
                    val imageSize = cursor.getLong(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))
                    val imageWidth = cursor.getInt(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[3]))
                    val imageHeight = cursor.getInt(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[4]))
                    val imageMimeType = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[5]))
                    val imageAddTime = cursor.getLong(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[6]))
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
                    if (!data.contains(imageFolder)) {
                        imageFolder.images = arrayListOf()
                        imageFolder.images.add(imageItem)
                        imageFolder.cover = imageItem
                        data.add(imageFolder)
                    } else {
                        data[data.indexOf(imageFolder)].images.add(imageItem)
                    }
                }
                if (cursor.count > 0 && allImages.size > 0) {
                    data.add(0, ImageFolder().apply {
                        name = context.resources.getString(R.string.picker_all_images)
                        path = "/"
                        cover = allImages[0]
                        images = allImages
                    })
                }
            }
        } finally {
            cursor?.close()
            return data
        }
    }

    override fun onStartLoading() {
        if (data.isEmpty()) {
            forceLoad()
        }
    }

    override fun onStopLoading() {
        cancelLoad()
    }

    override fun onContentChanged() {
        forceLoad()
    }

    override fun onReset() {
        super.onReset()
        cancelLoad()
        data.clear()
    }
}
