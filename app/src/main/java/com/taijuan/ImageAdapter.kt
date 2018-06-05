package com.taijuan

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.taijuan.data.ImageItem
import com.taijuan.imagepicker.R
import kotlinx.android.synthetic.main.item_image.view.*
import java.io.File

class ImageAdapter(private var images: List<ImageItem> = emptyList()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateData(images: List<ImageItem>) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        GlideApp.with(holder.itemView.context)
                .load(Uri.fromFile(File(images[position].path)))
                .placeholder(R.drawable.ic_default_image)
                .centerCrop()
                .into(holder.itemView.iv)

    }

    override fun getItemCount(): Int {
        return images.size
    }
}