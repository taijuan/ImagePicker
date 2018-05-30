package com.taijuan.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import com.github.chrisbanes.photoview.OnOutsidePhotoTapListener
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.taijuan.EXTRA_IMAGE_ITEMS
import com.taijuan.EXTRA_POSITION
import com.taijuan.ImagePicker.pickHelper
import com.taijuan.adapter.ImagePageAdapter
import com.taijuan.adapter.SmallPreviewAdapter
import com.taijuan.data.ImageItem
import com.taijuan.library.R
import com.taijuan.utils.color
import kotlinx.android.synthetic.main.activity_image_preview.*
import kotlinx.android.synthetic.main.include_top_bar.*

fun Activity.startForResultImagePreviewActivity(position: Int, data: MutableList<ImageItem> = pickHelper.selectedImages) {
    val intent = Intent(this, ImagePreviewActivity::class.java)
    intent.putExtra(EXTRA_POSITION, position)
    intent.putExtra(EXTRA_IMAGE_ITEMS, arrayListOf<ImageItem>().apply { addAll(data) })
    startActivityForResult(intent, REQUEST_PREVIEW)
}

class ImagePreviewActivity : BaseActivity(), View.OnClickListener, OnPhotoTapListener, OnOutsidePhotoTapListener {

    private var current: Int = 0
    private lateinit var previewAdapter: SmallPreviewAdapter
    private lateinit var imagePageAdapter: ImagePageAdapter
    private lateinit var data: MutableList<ImageItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)
        current = intent.getIntExtra(EXTRA_POSITION, 0)
        data = intent.getSerializableExtra(EXTRA_IMAGE_ITEMS) as ArrayList<ImageItem>
        imagePageAdapter = ImagePageAdapter(this, data)
        previewAdapter = SmallPreviewAdapter(this, data)
        btn_back.setOnClickListener { finish() }
        imagePageAdapter.listener = this
        viewpager.adapter = imagePageAdapter
        btn_ok.setOnClickListener(this)
        bottom_bar.visibility = View.VISIBLE
        tv_des.text = getString(R.string.ip_preview_image_count, current + 1, data.size)
        viewpager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                current = position
                cb_check.isChecked = contains(data[position])
                tv_des.text = getString(R.string.ip_preview_image_count, position + 1, data.size)
                updatePreview()
            }
        })
        viewpager.currentItem = current
        onCheckChanged()
        cb_check.isChecked = pickHelper.selectedImages.contains(data[current])
        cb_check.setOnClickListener {
            val imageItem = data[current]
            when {
                contains(imageItem) -> {
                    pickHelper.selectedImages.remove(imageItem)
                    cb_check.isChecked = false
                }
                pickHelper.canSelect() -> {
                    pickHelper.selectedImages.add(imageItem)
                    cb_check.isChecked = true
                }
                else -> {
                    cb_check.isChecked = false
                    Toast.makeText(applicationContext, getString(R.string.ip_select_limit, pickHelper.limit), Toast.LENGTH_SHORT).show()
                }
            }
            onCheckChanged()
            updatePreview()
        }

        rv_small.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        previewAdapter.listener = object : SmallPreviewAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, imageItem: ImageItem) {
                viewpager.setCurrentItem(data.indexOf(imageItem), false)
            }
        }
        rv_small.adapter = previewAdapter
        updatePreview()
    }

    private fun updatePreview() {
        if (data.size > 0) {
            rv_small.visibility = View.VISIBLE
            val index = data.indexOf(data[current])
            previewAdapter.current = if (index >= 0) data[index] else null
            if (index >= 0) {
                rv_small.smoothScrollToPosition(index)
            }
        } else {
            rv_small.visibility = View.GONE
        }
    }

    private fun onCheckChanged() {
        val selected: Int = pickHelper.selectedImages.size
        if (selected == 0) {
            btn_ok.isEnabled = false
            btn_ok.text = getString(R.string.ip_complete)
            btn_ok.setTextColor(color(R.color.ip_text_secondary_inverted))
        } else {
            btn_ok.isEnabled = true
            btn_ok.text = getString(R.string.ip_select_complete, selected, pickHelper.limit)
            btn_ok.setTextColor(color(R.color.ip_text_primary_inverted))
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_ok -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onOutsidePhotoTap(imageView: ImageView?) {
        changeTopAndBottomBar()
    }

    override fun onPhotoTap(view: ImageView?, x: Float, y: Float) {
        changeTopAndBottomBar()
    }

    private fun changeTopAndBottomBar() {
        if (top_bar.visibility == View.VISIBLE) {
            top_bar.animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            bottom_bar.animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            top_bar.visibility = View.GONE
            bottom_bar.visibility = View.GONE
        } else {
            top_bar.animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            bottom_bar.animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            top_bar.visibility = View.VISIBLE
            bottom_bar.visibility = View.VISIBLE
        }
    }

    private fun contains(imageItem: ImageItem): Boolean {
        return pickHelper.selectedImages.any { TextUtils.equals(it.path, imageItem.path) }
    }
}
