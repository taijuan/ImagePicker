package com.taijuan.data

import java.io.Serializable

class ImageItem : Serializable {
    /**
     * 图片的路径
     */
    var path: String? = null
    /**
     * 图片的名字
     */
    var name: String? = null
    /**
     * 图片的大小
     */
    var size: Long = 0
    /**
     * 图片的宽度
     */
    var width: Int = 0
    /**
     * 图片的高度
     */
    var height: Int = 0
    /**
     * 图片的类型
     */
    var mimeType: String? = null
    /**
     * 图片的创建时间
     */
    var addTime: Long = 0

    /**
     * 图片的路径和创建时间相同就认为是同一张图片
     */
    override fun equals(other: Any?): Boolean {
        if (other is ImageItem) {
            return this.path.equals(other.path, ignoreCase = true) && this.addTime == other.addTime
        }
        return super.equals(other)
    }
}
