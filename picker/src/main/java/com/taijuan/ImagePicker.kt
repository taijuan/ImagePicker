package com.taijuan

import android.content.Context
import com.taijuan.activity.startImageGridActivity
import com.taijuan.data.ImageItem
import com.taijuan.data.ImagePickHelper
import com.taijuan.loader.IMAGE_SELECTION
import com.taijuan.loader.VIDEO_SELECTION
import com.taijuan.widget.CropImageView
import kotlin.properties.Delegates

internal const val EXTRA_IMAGE_ITEMS = "extra_image_items"
internal const val EXTRA_POSITION = "extra_position"
internal const val EXTRA_TAKE_PHOTO = "extra_take_photo"

object ImagePicker {
    init {
        println("imagePicker init ...")
    }

    internal var imageLoader: ImageLoader by Delegates.notNull()
    internal var pickHelper: ImagePickHelper = ImagePickHelper()
    internal var listener: OnPickImageResultListener? = null

    /**
     * 在Application中初始化图片加载框架
     */
    @JvmStatic
    fun init(imageLoader: ImageLoader) {
        this.imageLoader = imageLoader
    }

    /**
     * 清楚缓存的已选择图片
     */
    @JvmStatic
    fun clear() {
        pickHelper.selectedImages.clear()
    }

    /**
     * 图片数量限制，默认9张
     */
    @JvmStatic
    fun limit(max: Int): ImagePicker {
        pickHelper.limit = max
        return this
    }

    /**
     * 是否显示相机，默认显示
     */
    @JvmStatic
    fun showCamera(boolean: Boolean): ImagePicker {
        pickHelper.isShowCamera = boolean
        return this
    }

    /**
     * 是否多选,默认显示
     */
    @JvmStatic
    fun multiMode(boolean: Boolean): ImagePicker {
        pickHelper.isMultiMode = boolean
        return this
    }

    /**
     * 是否裁剪，单选生效
     */
    @JvmStatic
    fun isCrop(boolean: Boolean): ImagePicker {
        pickHelper.isCrop = boolean
        return this
    }

    /**
     * 只展示图片
     */
    @JvmStatic
    fun imageOf(): ImagePicker {
        pickHelper.selection = IMAGE_SELECTION
        return this
    }

    /**
     * 只展示视频
     */
    @JvmStatic
    fun videoOf(): ImagePicker {
        pickHelper.selection = VIDEO_SELECTION
        return this
    }

    /**
     * 全部展示
     */
    @JvmStatic
    fun allOf(): ImagePicker {
        pickHelper.selection = "$IMAGE_SELECTION OR $VIDEO_SELECTION"
        return this
    }

    /**
     * @param focusStyle 裁剪框的形状
     * @param focusWidth 焦点框的宽度
     * @param focusHeight 焦点框的高度
     * @param outPutX 裁剪保存宽度
     * @param outPutY 裁剪保存高度
     * @param isSaveRectangle 裁剪后的图片是否是矩形，否者跟随裁剪框的形状
     */
    @JvmStatic
    fun CropConfig(focusStyle: CropImageView.Style, focusWidth: Int, focusHeight: Int, outPutX: Int, outPutY: Int, isSaveRectangle: Boolean) {
        pickHelper.focusStyle = focusStyle
        pickHelper.focusWidth = focusWidth
        pickHelper.focusHeight = focusHeight
        pickHelper.outPutX = outPutX
        pickHelper.outPutY = outPutY
        pickHelper.isSaveRectangle = isSaveRectangle
    }

    @JvmStatic
    fun pick(context: Context, listener: OnPickImageResultListener) {
        ImagePicker.listener = listener
        context.startImageGridActivity()
    }

    @JvmStatic
    fun camera(context: Context, listener: OnPickImageResultListener) {
        ImagePicker.listener = listener
        context.startImageGridActivity(true)
    }
}

interface OnPickImageResultListener {
    fun onImageResult(imageItems: MutableList<ImageItem>)
}
