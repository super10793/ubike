package com.demo.ubike.extension.view

import android.view.View
import android.view.ViewGroup

fun View.removeFromParent() {
    val viewGroup = (this.parent as? ViewGroup)
    viewGroup?.removeView(this)
}