package com.tayyar.tiletap.game

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.coroutineScope

open class BaseFrameLayout(context: Context, attributeSet: AttributeSet? = null) :
    FrameLayout(context, attributeSet) {

    protected val lifeCycleOwner by lazy { CustomLifeCycleOwner() }

    private var isActive = false
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lifeCycleOwner.startListening()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lifeCycleOwner.stopListening()
    }

    override fun onVisibilityAggregated(isVisible: Boolean) {
        isActive = isVisible
        super.onVisibilityAggregated(isVisible)
    }

    fun executeIfActive(block: () -> Unit) {
        if (isActive)
            block()
    }
}

class CustomLifeCycleOwner : LifecycleOwner {
    private val mLifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    val customViewLifeCycleScope: LifecycleCoroutineScope
        get() = lifecycle.coroutineScope


    fun stopListening() {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    fun startListening() {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    init {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }


}