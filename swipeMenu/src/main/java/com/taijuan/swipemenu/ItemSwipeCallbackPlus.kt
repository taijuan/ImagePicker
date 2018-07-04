package com.taijuan.swipemenu

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log

class ItemSwipeCallbackPlus : ItemSwipeTouchHelperPlus.Callback() {

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return if (viewHolder is ItemSwipeListener) {
            ItemSwipeTouchHelperPlus.Callback.makeMovementFlags(0, ItemTouchHelper.START)
        } else {
            super.getAbsoluteMovementFlags(recyclerView, viewHolder)
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (viewHolder is ItemSwipeListener) {
            val actionWidth = viewHolder.swipeWidth
            val dx = if (dX > 0 && dX > actionWidth) {
                actionWidth
            } else if (dX < 0 && -dX > actionWidth) {
                -actionWidth
            } else {
                dX
            }
            (viewHolder as ItemSwipeListener).onSwipe(dx)
        }
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder?): Float {
        return if (viewHolder is ItemSwipeListener) .5f else super.getSwipeThreshold(viewHolder)
    }
}
