package com.mr3y.ludi.shared.ui.presenter

import androidx.compose.runtime.BroadcastFrameClock
import kotlin.coroutines.CoroutineContext

internal actual fun frameClock(): CoroutineContext = BroadcastFrameClock()
