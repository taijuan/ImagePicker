package com.taijuan.data


class ImageFolder {
    /**
     * 当前文件夹的名字
     */
    var name: String? = null
    /**
     * 当前文件夹的路径
     */
    var path: String? = null
    /**
     * 当前文件夹需要要显示的缩略图，默认为最近的一次图片
     */
    var cover: ImageItem? = null
    /**
     * 当前文件夹下所有图片的集合
     */
    var images = arrayListOf<ImageItem>()

    /**
     * 只要文件夹的路径和名字相同，就认为是相同的文件夹
     */
    override fun equals(other: Any?): Boolean {
        if (other is ImageFolder) {
            return this.path.equals(other.path, ignoreCase = true) && this.name.equals(other.name, ignoreCase = true)
        }
        return super.equals(other)
    }
}
