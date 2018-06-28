package com.taijuan.swipemenu;

import android.view.View;

public interface SwipeListener {

    float getSwipeWidth();

    void onSwipe(float dx);

    View getSwipeView();
}
