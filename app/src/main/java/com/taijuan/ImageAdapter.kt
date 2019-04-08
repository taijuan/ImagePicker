package com.taijuan

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.taijuan.data.ImageItem
import com.taijuan.imagepicker.R
import kotlinx.android.synthetic.main.item_image.view.*
import java.io.File

class ImageAdapter(private var images: List<ImageItem> = emptyList()) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    fun updateData(images: List<ImageItem>) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return object : androidx.recyclerview.widget.RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)) {}

    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
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