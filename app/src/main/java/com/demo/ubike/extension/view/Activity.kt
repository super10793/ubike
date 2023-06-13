package com.demo.ubike.extension.view

import android.app.Activity
import android.graphics.Rect

fun Activity.getStatusBarHeight(): Int? {
    fun getHeightByResources(): Int? {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            null
        }
    }

    fun getHeightUseRect(): Int? {
        val rectangle = Rect()
        this.window.decorView.getWindowVisibleDisplayFrame(rectangle)
        val statusBarHeight = rectangle.top
        return if (statusBarHeight != 0) statusBarHeight else null
    }

    return getHeightByResources() ?: getHeightUseRect()
}

fun Activity.getNavigationBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        val a = resources.getDimensionPixelSize(resourceId)
        a
    } else {
        0
    }
}

/* 需要使用非同步才能取得高度
fun getStatusBarHeight(): Int? {
    window.decorView.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            val top = ViewCompat.getRootWindowInsets(window.decorView)?.getInsets(
                WindowInsetsCompat.Type.statusBars()
            )?.top

            val bottom = ViewCompat.getRootWindowInsets(window.decorView)?.getInsets(
                WindowInsetsCompat.Type.statusBars()
            )?.bottom

            var result: Int? = null
            if (top != null && bottom != null) {
                result = abs(bottom - top)
            }
        }

        override fun onViewDetachedFromWindow(v: View) {
            //
        }
    })

    return null
}
*/
