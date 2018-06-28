package com.taijuan

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.taijuan.imagepicker.R
import com.taijuan.swipemenu.ItemCallbackPlus
import com.taijuan.swipemenu.ItemTouchHelperPlus
import com.taijuan.swipemenu.SwipeListener
import kotlinx.android.synthetic.main.activity_swipe_demo.*
import kotlinx.android.synthetic.main.item_swipe.view.*

class SwipeDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_demo)
        recyclerView.layoutManager = LinearLayoutManager(this)
        ItemTouchHelperPlus(ItemCallbackPlus()).attachToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
            setDrawable(ColorDrawable(Color.parseColor("#f05555")).apply {
                setBounds(0, 0, 3, 3)
            })
        })
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
                return object : RecyclerView.ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_swipe, p0, false)), SwipeListener {
                    init {
                        itemView.test.setOnClickListener {
                            Toast.makeText(it.context, "测试", Toast.LENGTH_SHORT).show()
                        }
                        itemView.num.setOnClickListener {
                            if (swipeView.translationX == 0f) {
                                Toast.makeText(it.context, "测试${itemView.num.text}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun getSwipeWidth(): Float {
                        return itemView.test.measuredWidth.toFloat()
                    }

                    override fun onSwipe(dx: Float) {
                        itemView.test.translationX = dx
                    }

                    override fun getSwipeView(): View {
                        return itemView.test
                    }
                }
            }

            override fun getItemCount() = 20

            override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
                p0.itemView.num.text = "$p1"
            }
        }
    }
}