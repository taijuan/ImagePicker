package com.taijuan.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.GridLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import com.taijuan.EXTRA_TAKE_PHOTO
import com.taijuan.ImagePicker
import com.taijuan.ImagePicker.pickHelper
import com.taijuan.adapter.ImageFolderAdapter
import com.taijuan.adapter.ImageRecyclerAdapter
import com.taijuan.data.ImageDataSource
import com.taijuan.data.ImageFolder
import com.taijuan.data.ImageItem
import com.taijuan.library.R
import com.taijuan.utils.color
import com.taijuan.utils.takePicture
import com.taijuan.widget.FolderPopUpWindow
import kotlinx.android.synthetic.main.activity_image_grid.*
import kotlinx.android.synthetic.main.include_top_bar.*
import java.io.File

internal const val REQUEST_PERMISSION_STORAGE = 0x12
internal const val REQUEST_PERMISSION_CAMERA = 0x13
internal const val REQUEST_CAMERA = 0x23
internal const val REQUEST_PREVIEW = 0x9
internal const val REQUEST_CROP = 0x10

/**
 * @param takePhoto 是否直接开启拍照
 */
fun Context.startImageGridActivity(takePhoto: Boolean = false) {
    val intent = Intent(this, ImageGridActivity::class.java)
    intent.putExtra(EXTRA_TAKE_PHOTO, takePhoto)
    startActivity(intent)
}

class ImageGridActivity : BaseActivity(), View.OnClickListener, ImageDataSource.OnImagesLoadedListener, ImageRecyclerAdapter.OnImageItemClickListener {
    private val imageDataSource = ImageDataSource(this)
    private lateinit var adapter: ImageRecyclerAdapter
    private lateinit var mFolderPopupWindow: FolderPopUpWindow
    private var imageFolders: ArrayList<ImageFolder> = arrayListOf()
    private lateinit var takeImageFile: File
    private var index: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_grid)
        if (intent.getBooleanExtra(EXTRA_TAKE_PHOTO, false)) {
            onCameraClick()
        }
        initView()
        loadData()
    }

    private fun loadData() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_STORAGE)
        } else {
            imageDataSource.loadImage(this)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
        onCheckChanged(ImagePicker.pickHelper.selectedImages.size, ImagePicker.pickHelper.limit)
    }

    private fun initView() {
        btn_ok.setOnClickListener(this)
        btn_back.setOnClickListener(this)
        btn_preview.setOnClickListener(this)
        tv_dir.setOnClickListener(this)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        adapter = ImageRecyclerAdapter(this)
        recyclerView.adapter = adapter
        adapter.listener = this
        if (ImagePicker.pickHelper.isMultiMode) {
            btn_ok.visibility = View.VISIBLE
            btn_preview.visibility = View.VISIBLE
        } else {
            btn_ok.visibility = View.GONE
            btn_preview.visibility = View.GONE
        }
    }

    private fun showPopupFolderList() {
        mFolderPopupWindow = FolderPopUpWindow(this, ImageFolderAdapter(this, imageFolders, index))
        mFolderPopupWindow.setOnItemClickListener(object : FolderPopUpWindow.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>, view: View, position: Int, l: Long) {
                this@ImageGridActivity.index = position
                mFolderPopupWindow.dismiss()
                val imageFolder = adapterView.adapter?.getItem(position) as ImageFolder
                adapter.refreshData(imageFolder.images)
                tv_dir.text = imageFolder.name
            }
        })
        mFolderPopupWindow.showAtLocation(window.decorView, Gravity.BOTTOM, 0, 0)
        mFolderPopupWindow.setSelection(this.index)
    }

    override fun onCheckChanged(selected: Int, limit: Int) {
        if (selected == 0) {
            btn_ok.isEnabled = false
            btn_ok.text = getString(R.string.picker_complete)
            btn_ok.setTextColor(color(R.color.ip_text_secondary_inverted))
            btn_preview.isEnabled = false
            btn_preview.text = getString(R.string.picker_preview)
            btn_preview.setTextColor(color(R.color.ip_text_secondary_inverted))
        } else {
            btn_ok.isEnabled = true
            btn_ok.text = getString(R.string.picker_select_complete, selected, limit)
            btn_ok.setTextColor(color(R.color.ip_text_primary_inverted))
            btn_preview.isEnabled = true
            btn_preview.text = getString(R.string.picker_preview_count, selected)
            btn_preview.setTextColor(color(R.color.ip_text_primary_inverted))
        }
    }

    override fun onCameraClick() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION_CAMERA)
        } else {
            takeImageFile = takePicture(this, REQUEST_CAMERA)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageDataSource.loadImage(this)
            } else {
                showToast(getString(R.string.picker_permission_storage))
            }
        } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takeImageFile = takePicture(this, REQUEST_CAMERA)
            } else {
                showToast(getString(R.string.picker_permission_camera))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { it.data = Uri.fromFile(takeImageFile) })
            val imageItem = ImageItem().apply {
                path = takeImageFile.absolutePath
            }
            if (pickHelper.isMultiMode) {
                ImagePicker.pickHelper.selectedImages.add(imageItem)
            } else {
                ImagePicker.pickHelper.selectedImages.clear()
                ImagePicker.pickHelper.selectedImages.add(imageItem)
                if (ImagePicker.pickHelper.isCrop) {//需要裁剪
                    startImageCropActivity()
                } else {
                    setResult()
                }
            }
        } else if (requestCode == REQUEST_PREVIEW && resultCode == Activity.RESULT_OK) {
            setResult()
        } else if (requestCode == REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            finish()
        } else {
            if (intent.getBooleanExtra(EXTRA_TAKE_PHOTO, false) && !pickHelper.isMultiMode) {
                finish()
            }
        }
    }

    private fun setResult() {
        ImagePicker.listener?.onImageResult(pickHelper.selectedImages)
        ImagePicker.listener = null
        finish()
    }

    override fun onClick(v: View) {
        when (v) {
            tv_dir -> showPopupFolderList()
            btn_ok -> setResult()
            btn_preview -> startForResultImagePreviewActivity(0)
            btn_back -> finish()

        }
    }

    override fun onImagesLoaded(imageFolders: ArrayList<ImageFolder>) {
        this.imageFolders.addAll(imageFolders)
        if (this.imageFolders.isNotEmpty()) {
            this.index = 0
            adapter.refreshData(this.imageFolders[0].images)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        imageDataSource.destroyLoader()
    }
}
