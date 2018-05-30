package com.taijuan.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import com.taijuan.ImagePicker
import com.taijuan.ImagePicker.pickHelper
import com.taijuan.data.ImageItem
import com.taijuan.library.R
import com.taijuan.utils.dp2px
import com.taijuan.utils.getBitmapDegree
import com.taijuan.utils.getCropCacheFolder
import com.taijuan.widget.CropImageView
import kotlinx.android.synthetic.main.activity_image_crop.*
import kotlinx.android.synthetic.main.include_top_bar.*
import java.io.File

fun Activity.startImageCropActivity() {
    startActivityForResult(Intent(this, ImageCropActivity::class.java), REQUEST_CROP)
}

class ImageCropActivity : BaseActivity(), View.OnClickListener, CropImageView.OnBitmapSaveCompleteListener {

    private var mBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_crop)
        btn_back.setOnClickListener(this)
        tv_des.text = getString(R.string.ip_photo_crop)
        btn_ok.text = getString(R.string.ip_complete)
        btn_ok.setOnClickListener(this)
        cv_crop_image.setOnBitmapSaveCompleteListener(this)
        cv_crop_image.focusStyle = pickHelper.focusStyle
        cv_crop_image.focusWidth = dp2px(this, pickHelper.focusWidth.toFloat())
        cv_crop_image.focusHeight = dp2px(this, pickHelper.focusHeight.toFloat())
        val imagePath = pickHelper.selectedImages[0].path
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, options)
        val displayMetrics = resources.displayMetrics
        options.inSampleSize = calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels)
        options.inJustDecodeBounds = false
        mBitmap = BitmapFactory.decodeFile(imagePath, options)
        cv_crop_image.setImageBitmap(cv_crop_image.rotate(mBitmap, getBitmapDegree(imagePath)))
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val width = options.outWidth
        val height = options.outHeight
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            inSampleSize = if (width > height) {
                width / reqWidth
            } else {
                height / reqHeight
            }
        }
        return inSampleSize
    }

    override fun onClick(v: View) {
        when (v) {
            btn_back -> finish()
            btn_ok -> cv_crop_image.saveBitmapToFile(getCropCacheFolder(this), pickHelper.outPutX, pickHelper.outPutY, pickHelper.isSaveRectangle)
        }
    }

    override fun onBitmapSaveSuccess(file: File?) {
        ImagePicker.listener?.onImageResult(pickHelper.selectedImages.also {
            it.clear()
            it.add(ImageItem().apply {
                path = file?.absolutePath
            })
        })
        ImagePicker.listener = null
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onBitmapSaveError(file: File?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        cv_crop_image.setOnBitmapSaveCompleteListener(null)
        if (mBitmap?.isRecycled == false) {
            mBitmap?.recycle()
            mBitmap = null
        }
    }
}