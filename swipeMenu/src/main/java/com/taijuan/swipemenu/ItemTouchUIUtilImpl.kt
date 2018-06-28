/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taijuan.swipemenu

import android.graphics.Canvas
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchUIUtil
import android.view.View

internal class ItemTouchUIUtilImpl {
    internal class Lollipop : Honeycomb() {
        override fun onDraw(c: Canvas, recyclerView: RecyclerView, view: View, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            if (isCurrentlyActive) {
                var originalElevation: Any? = view.getTag(R.id.item_touch_helper_previous_elevation)
                if (originalElevation == null) {
                    originalElevation = ViewCompat.getElevation(view)
                    val newElevation = 1f + findMaxElevation(recyclerView, view)
                    ViewCompat.setElevation(view, newElevation)
                    view.setTag(R.id.item_touch_helper_previous_elevation, originalElevation)
                }
            }
            super.onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive)
        }

        private fun findMaxElevation(recyclerView: RecyclerView, itemView: View): Float {
            val childCount = recyclerView.childCount
            var max = 0f
            for (i in 0 until childCount) {
                val child = recyclerView.getChildAt(i)
                if (child === itemView) {
                    continue
                }
                val elevation = ViewCompat.getElevation(child)
                if (elevation > max) {
                    max = elevation
                }
            }
            return max
        }

        override fun clearView(view: View) {
            val tag = view.getTag(R.id.item_touch_helper_previous_elevation)
            if (tag is Float) {
                ViewCompat.setElevation(view, tag)
            }
            view.setTag(R.id.item_touch_helper_previous_elevation, null)
            super.clearView(view)
        }
    }

    internal open class Honeycomb : ItemTouchUIUtil {

        override fun clearView(view: View) {
            view.translationX = 0f
            view.translationY = 0f
        }

        override fun onSelected(view: View) {
        }

        override fun onDraw(c: Canvas, recyclerView: RecyclerView, view: View, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            view.translationX = dX
            view.translationY = dY
        }

        override fun onDrawOver(c: Canvas, recyclerView: RecyclerView, view: View, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        }
    }
}
