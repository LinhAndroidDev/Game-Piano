package com.tayyar.tiletap.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.receiveAsFlow

object GlobalCounter {

    @OptIn(ObsoleteCoroutinesApi::class)
    private val enemyTimer = ticker(35, 1000, Dispatchers.Default)

    val enemyTimerFlow = enemyTimer.receiveAsFlow()

    @OptIn(ObsoleteCoroutinesApi::class)
    private val starsBackgroundTimer = ticker(30, 1000, Dispatchers.Default)

    val starsBackgroundTimerFlow = starsBackgroundTimer.receiveAsFlow()

}