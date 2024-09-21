package com.tayyar.tiletap.utils

import android.content.Context
import android.content.res.ColorStateList
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tayyar.tiletap.R

fun ImageView.highlight(context: Context) {
    val starPulse = AnimationUtils.loadAnimation(context, R.anim.enter_from_center)
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.yellow))
    startAnimation(starPulse)
}

fun ImageView.disable(context: Context) {
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grey_dark))
}