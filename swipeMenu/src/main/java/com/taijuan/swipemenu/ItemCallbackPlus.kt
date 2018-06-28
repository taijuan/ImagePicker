package com.taijuan.swipemenu

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log


/**
 * Created by WANG on 18/3/14.
 */

class ItemCallbackPlus : ItemTouchHelperPlus.Callback() {

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return if (viewHolder is SwipeListener) {
            ItemTouchHelperPlus.Callback.makeMovementFlags(0, ItemTouchHelper.START)
        } else {
            super.getAbsoluteMovementFlags(recyclerView, viewHolder)
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (viewHolder is SwipeListener) {
            val actionWidth = viewHolder.swipeWidth
            Log.e("zuiweng", dX.toString())
            Log.e("zuiweng", actionWidth.toString())
            val dx = if (dX > 0 && dX > actionWidth) {
                actionWidth
            } else if (dX < 0 && -dX > actionWidth) {
                -actionWidth
            } else {
                dX
            }
            (viewHolder as SwipeListener).onSwipe(dx)
        }
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder?): Float {
        return if (viewHolder is SwipeListener) .5f else super.getSwipeThreshold(viewHolder)
    }
}
