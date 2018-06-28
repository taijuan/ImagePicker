package com.taijuan.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.ColorRes
import android.support.media.ExifInterface
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.TypedValue
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 判断SDCard是否可用
 */
internal fun existSDCard(): Boolean {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}

/**
 * dp转px
 */
fun dp2px(context: Context, dpVal: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics).toInt()
}

/**
 * 获取裁剪图片保存文件夹
 */
internal fun getCropCacheFolder(context: Context): File {
    return File(context.cacheDir.toString() + "/ImagePicker/cropTemp/")
}

/**
 * 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
 * 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
 * 可以通过dat extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
 * 如果没有指定uri，则data就返回有数据！
 *
 * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
 */
internal fun takePicture(activity: Activity, requestCode: Int): File {
    var takeImageFile = if (existSDCard()) {
        File(Environment.getExternalStorageDirectory(), "/DCIM/camera/")
    } else {
        Environment.getDataDirectory()
    }
    takeImageFile = createFile(takeImageFile, "IMG_", ".jpg")
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    takePictureIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
        val uri = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            Uri.fromFile(takeImageFile)
        } else {
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            FileProvider.getUriForFile(activity, "${activity.packageName}.provider", takeImageFile)
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        activity.startActivityForResult(takePictureIntent, requestCode)
    }
    return takeImageFile
}

/**
 * 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
 * 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
 * 可以通过dat extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
 * 如果没有指定uri，则data就返回有数据！
 *
 * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
 */
internal fun takeVideo(activity: Activity, requestCode: Int): File {
    var takeImageFile = if (existSDCard()) {
        File(Environment.getExternalStorageDirectory(), "/DCIM/camera/")
    } else {
        Environment.getDataDirectory()
    }
    takeImageFile = createFile(takeImageFile, "IMG_", ".mp4")
    val takePictureIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
    takePictureIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
        val uri = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            Uri.fromFile(takeImageFile)
        } else {
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            FileProvider.getUriForFile(activity, "${activity.packageName}.provider", takeImageFile)
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        activity.startActivityForResult(takePictureIntent, requestCode)
    }
    return takeImageFile
}

/**
 * 根据系统时间、前缀、后缀产生一个文件
 */
internal fun createFile(folder: File, prefix: String, suffix: String): File {
    if (!folder.exists() || !folder.isDirectory) folder.mkdirs()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.CHINA)
    val filename = prefix + dateFormat.format(Date(System.currentTimeMillis())) + suffix
    return File(folder, filename)
}

/**
 * 获取图片的旋转角度
 *
 * @param path 图片绝对路径
 * @return 图片的旋转角度
 */
fun getBitmapDegree(path: String?): Int {
    var degree = 0
    if (path != null) {
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return degree
}

fun Context.color(@ColorRes res: Int) = ContextCompat.getColor(this, res)