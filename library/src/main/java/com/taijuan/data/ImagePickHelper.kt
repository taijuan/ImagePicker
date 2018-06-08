package com.taijuan.data

import com.taijuan.loader.IMAGE_SELECTION
import com.taijuan.widget.CropImageView

internal class ImagePickHelper {
    /**
     * 选择照片限制
     */
    var limit: Int = 9
    /**
     * 是否裁剪
     */
    var isCrop: Boolean = false
    /**
     * 是否显示拍照按钮
     */
    var isShowCamera: Boolean = true
    /**
     * 选择模式
     */
    var isMultiMode: Boolean = true
    /**
     * 裁剪框的形状
     */
    var focusStyle = CropImageView.Style.RECTANGLE
    /**
     * 裁剪保存宽度
     */
    var outPutX = 800
    /**
     * 裁剪保存宽度
     */
    var outPutY = 800
    /**
     * 焦点框的宽度
     */
    var focusWidth = 280
    /**
     * 焦点框的高度
     */
    var focusHeight = 280
    /**
     * 裁剪后的图片是否是矩形，否者跟随裁剪框的形状
     */
    var isSaveRectangle = false
    /**
     * 查询筛选条件
     */
    var selection: String = IMAGE_SELECTION
    /**
     * 已经选中的图片数据
     */
    val selectedImages = mutableListOf<ImageItem>()

    /**
     * 是否能继续选择
     */
    fun canSelect(): Boolean = selectedImages.size < limit
}