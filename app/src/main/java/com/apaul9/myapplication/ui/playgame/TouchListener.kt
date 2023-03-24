package com.apaul9.myapplication.ui.playgame

import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout


class TouchListener : View.OnTouchListener {
    private var xDelta = 0f
    private var yDelta = 0f

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val x = motionEvent.rawX
        val y = motionEvent.rawY
        val lParams = view.getLayoutParams() as RelativeLayout.LayoutParams
        when (motionEvent.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                xDelta = x - lParams.leftMargin
                yDelta = y - lParams.topMargin
            }
            MotionEvent.ACTION_MOVE -> {
                lParams.leftMargin = (x - xDelta).toInt()
                lParams.topMargin = (y - yDelta).toInt()
                view.setLayoutParams(lParams)
            }
        }
        return true
    }
}