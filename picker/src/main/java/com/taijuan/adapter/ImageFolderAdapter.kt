package com.taijuan.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.taijuan.ImagePicker
import com.taijuan.data.ImageFolder
import com.taijuan.picker.R
import com.taijuan.picker.R.string.picker_folder_image_count
import kotlinx.android.synthetic.main.adapter_folder_list_item.view.*

internal class ImageFolderAdapter(private val activity: Activity, private val folders: ArrayList<ImageFolder> = arrayListOf(), private val selectIndex: Int = 0) : BaseAdapter() {

    override fun getCount(): Int = folders.size

    override fun getItem(position: Int): ImageFolder = folders[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder = if (convertView == null) {
            ViewHolder(parent)
        } else {
            convertView.tag as ViewHolder
        }
        val item = getItem(position)
        holder.folderName.text = item.name
        holder.imageCount.text = activity.getString(picker_folder_image_count, item.images.size)
        ImagePicker.imageLoader.displayImage(activity, item.cover?.path, holder.cover, holder.cover.measuredWidth, holder.cover.measuredHeight)
        if (selectIndex == position) {
            holder.folderCheck.visibility = View.VISIBLE
        } else {
            holder.folderCheck.visibility = View.INVISIBLE
        }
        return holder.contentView
    }

    private inner class ViewHolder(parent: ViewGroup) {
        internal var contentView: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_folder_list_item, parent, false)
        internal var cover: ImageView
        internal var folderName: TextView
        internal var imageCount: TextView
        internal var folderCheck: ImageView

        init {
            contentView.tag = this
            cover = contentView.iv_cover
            folderName = contentView.tv_folder_name
            imageCount = contentView.tv_image_count
            folderCheck = contentView.iv_folder_check
        }
    }
}
