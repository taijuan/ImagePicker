package com.taijuan

import android.app.Application

class ImagePickerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ImagePicker.init(GlideImageLoader())
        ImagePicker.limit(12).isCrop(true)
    }
}