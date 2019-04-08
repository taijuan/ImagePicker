package com.taijuan.activity

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

internal open class BaseActivity : AppCompatActivity() {
    fun showToast(charSequence: CharSequence) {
        Toast.makeText(this, charSequence, Toast.LENGTH_SHORT).show()
    }
}