package com.taijuan.data

import java.io.Serializable

class ImageItem : Serializable {
    /**
     * 路径
     */
    var path: String? = null
    /**
     * 名字
     */
    var name: String? = null
    /**
     * 大小
     */
    var size: Long = 0
    /**
     * 宽度
     */
    var width: Int = 0
    /**
     * 高度
     */
    var height: Int = 0
    /**
     * 类型
     */
    var mimeType: String? = null
    /**
     * 创建时间
     */
    var addTime: Long = 0

    /**
     * 路径和就认为是同一张图片
     */
    override fun equals(other: Any?): Boolean {
        if (other is ImageItem) {
            return this.path.equals(other.path, ignoreCase = true)
        }
        return super.equals(other)
    }

    /**
     * 判断是否是图片资源
     */
    fun isImage(): Boolean {
        return mimeType?.contains("image", true) ?: false
    }
}
