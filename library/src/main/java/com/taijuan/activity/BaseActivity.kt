package com.taijuan.activity

import android.support.v7.app.AppCompatActivity
import android.widget.Toast

internal open class BaseActivity : AppCompatActivity() {
    fun showToast(charSequence: CharSequence) {
        Toast.makeText(this, charSequence, Toast.LENGTH_SHORT).show()
    }
}