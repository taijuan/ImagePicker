package com.taijuan

import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import com.taijuan.imagepicker.R
import java.io.File

/**
 * Created by hubert
 *
 * Created on 2017/10/12.
 */
class GlideImageLoader : ImageLoader {

    override fun displayImage(activity: Activity, path: String?, imageView: ImageView, width: Int, height: Int) {
        GlideApp.with(activity)                             //配置上下文
                .load(Uri.fromFile(File(path)))
                .centerCrop()//设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .error(R.drawable.ic_default_image)           //设置错误图片
                .placeholder(R.drawable.ic_default_image)     //设置占位图片
                .into(imageView)
    }

    override fun displayImagePreview(activity: Activity, path: String?, imageView: ImageView, width: Int, height: Int) {
        GlideApp.with(activity)                             //配置上下文
                .load(Uri.fromFile(File(path)))
                .fitCenter()//设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .into(imageView)
    }

    override fun clearMemoryCache() {

    }
}