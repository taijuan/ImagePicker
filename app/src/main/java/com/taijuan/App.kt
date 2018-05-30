package com.taijuan

import android.app.Application

/**
 * Created by hubert
 *
 * Created on 2017/10/24.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ImagePicker.init(GlideImageLoader())
        ImagePicker.limit(12).isCrop(true)
    }
}