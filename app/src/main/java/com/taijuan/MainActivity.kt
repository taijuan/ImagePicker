package com.taijuan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.taijuan.data.ImageItem
import com.taijuan.imagepicker.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnPickImageResultListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ImagePicker.allOf()
        cb_crop.setOnCheckedChangeListener { _, isChecked -> ImagePicker.isCrop(isChecked) }
        cb_multi.isChecked = true//默认是多选
        cb_multi.setOnCheckedChangeListener { _, isChecked -> ImagePicker.multiMode(isChecked) }
        btn_pick.setOnClickListener {
            //选择图片，第二次进入会自动带入之前选择的图片（未重置图片参数）
            ImagePicker.pick(this@MainActivity, this@MainActivity)
        }
        btn_camera.setOnClickListener {
            //直接打开相机
            ImagePicker.camera(this@MainActivity, this@MainActivity)
        }
        recycler_view.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)
        recycler_view.adapter = ImageAdapter()
    }

    override fun onImageResult(imageItems: MutableList<ImageItem>) {
        (recycler_view.adapter as ImageAdapter).updateData(imageItems)
    }

    override fun onDestroy() {
        super.onDestroy()
        ImagePicker.clear()//清除缓存已选择的图片
    }
}
