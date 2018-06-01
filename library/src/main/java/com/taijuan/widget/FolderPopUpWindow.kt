package com.taijuan.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.PopupWindow
import com.taijuan.library.R
import com.taijuan.utils.dp2px
import kotlinx.android.synthetic.main.pop_folder.view.*

class FolderPopUpWindow(context: Context, adapter: BaseAdapter) : PopupWindow(context) {

    private var onItemClickListener: OnItemClickListener? = null

    init {
        width = context.resources.displayMetrics.widthPixels
        contentView = View.inflate(context, R.layout.pop_folder, null)
        contentView.listView.adapter = adapter
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(0))
        height = WindowManager.LayoutParams.MATCH_PARENT
        if (adapter.count > 3) {
            contentView.listView.layoutParams.height = dp2px(context, 360f)
        }
        contentView.setOnClickListener { dismiss() }
        contentView.listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, v, position, l -> onItemClickListener?.onItemClick(adapterView, v, position, l) }
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        super.showAtLocation(parent, gravity, x, y)
        contentView.listView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                contentView.listView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val dy = contentView.listView.measuredHeight.toFloat()
                contentView.listView.translationY = dy
                contentView.listView.animate().translationY(0f).setDuration(300).start()
            }
        })
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    fun setSelection(selection: Int) {
        contentView.listView.setSelection(selection)
    }

    interface OnItemClickListener {
        fun onItemClick(adapterView: AdapterView<*>, view: View, position: Int, l: Long)
    }
}
