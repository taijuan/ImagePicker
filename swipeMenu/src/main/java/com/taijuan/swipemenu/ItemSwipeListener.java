package com.taijuan.swipemenu;

import android.view.View;

public interface ItemSwipeListener {

    float getSwipeWidth();

    void onSwipe(float dx);

    View getSwipeView();
}
