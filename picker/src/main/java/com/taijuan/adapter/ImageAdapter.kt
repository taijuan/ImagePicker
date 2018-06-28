package com.taijuan.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.taijuan.ImagePicker
import com.taijuan.ImagePicker.pickHelper
import com.taijuan.activity.startForResultImagePreviewActivity
import com.taijuan.activity.startImageCropActivity
import com.taijuan.data.ImageItem
import com.taijuan.loader.IMAGE_SELECTION
import com.taijuan.loader.VIDEO_SELECTION
import com.taijuan.picker.R
import kotlinx.android.synthetic.main.adapter_image_list_item.view.*
import kotlin.properties.Delegates

private const val ITEM_TYPE_CAMERA = 0  //第一个条目是相机
private const val ITEM_TYPE_NORMAL = 1  //第一个条目不是相机

internal class ImageAdapter(private val activity: Activity, private val images: ArrayList<ImageItem> = arrayListOf()) : RecyclerView.Adapter<ViewHolder>() {

    internal var listener: OnImageItemClickListener by Delegates.notNull()

    interface OnImageItemClickListener {
        fun onCheckChanged(selected: Int, limit: Int)
        fun onCameraClick()
    }

    fun refreshData(images: ArrayList<ImageItem>) {
        this.images.clear()
        this.images.addAll(images)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == ITEM_TYPE_CAMERA) CameraViewHolder(parent) else ImageViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> holder.bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int = if (pickHelper.isShowCamera && position == 0) ITEM_TYPE_CAMERA else ITEM_TYPE_NORMAL

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int = if (pickHelper.isShowCamera) images.size + 1 else images.size

    fun getItem(position: Int): ImageItem = if (pickHelper.isShowCamera && position != 0) images[position - 1] else images[position]


    private inner class ImageViewHolder(parent: ViewGroup) : ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_image_list_item, parent, false)) {

        internal fun bind(position: Int) {
            val imageItem = getItem(position)
            ImagePicker.imageLoader.displayImage(activity, imageItem.path, itemView.iv_thumb, itemView.iv_thumb.measuredWidth, itemView.iv_thumb.measuredWidth) //显示图片
            itemView.cb_check.visibility = if (pickHelper.isMultiMode) View.VISIBLE else View.GONE
            itemView.cb_check.isChecked = contains(imageItem)
            itemView.mask.visibility = if (pickHelper.isMultiMode && contains(imageItem)) View.VISIBLE else View.GONE
            if (pickHelper.selection == IMAGE_SELECTION || pickHelper.selection == VIDEO_SELECTION) {
                itemView.mimeType.visibility = View.GONE
            } else {
                itemView.mimeType.text = imageItem.mimeType
                itemView.mimeType.visibility = View.VISIBLE
            }
            itemView.iv_thumb.setOnClickListener {
                if (!pickHelper.isMultiMode) {
                    pickHelper.selectedImages.also {
                        it.clear()
                        it.add(imageItem)
                    }
                    if (pickHelper.isCrop && imageItem.isImage()) {
                        activity.startImageCropActivity()
                    } else {
                        ImagePicker.listener?.onImageResult(pickHelper.selectedImages)
                        ImagePicker.listener = null
                        activity.finish()
                    }
                } else {
                    activity.startForResultImagePreviewActivity(if (pickHelper.isShowCamera) position - 1 else position, images)
                }
            }
            itemView.cb_check.setOnClickListener {
                when {
                    contains(imageItem) -> {
                        pickHelper.selectedImages.remove(imageItem)
                        itemView.mask.visibility = View.GONE
                        itemView.cb_check.isChecked = false
                    }
                    pickHelper.canSelect() -> {
                        itemView.mask.visibility = View.VISIBLE
                        pickHelper.selectedImages.add(imageItem)
                        itemView.cb_check.isChecked = true
                    }
                    else -> {
                        Toast.makeText(activity.applicationContext, activity.getString(R.string.picker_select_limit, pickHelper.limit), Toast.LENGTH_SHORT).show()
                        itemView.cb_check.isChecked = false
                    }
                }
                listener.onCheckChanged(pickHelper.selectedImages.size, pickHelper.limit)
            }
        }

        private fun contains(imageItem: ImageItem): Boolean {
            return pickHelper.selectedImages.any { it == imageItem }
        }
    }

    private inner class CameraViewHolder(parent: ViewGroup) : ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_camera_item, parent, false)) {
        init {
            itemView.setOnClickListener { listener.onCameraClick() }
        }
    }
}
