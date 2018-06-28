package com.taijuan.dialog

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.taijuan.picker.R
import kotlinx.android.synthetic.main.dialog_camera_select.*

class CameraDialog(context: Context, private val listener: (Int) -> Unit) : AlertDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_camera_select)
        takePicture.setOnClickListener {
            listener.invoke(R.id.takePicture)
            dismiss()
        }
        recordVideo.setOnClickListener {
            listener.invoke(R.id.recordVideo)
            dismiss()
        }
    }
}