package com.mr3y.ludi.shared.ui.presenter

import androidx.compose.ui.platform.AndroidUiDispatcher
import kotlin.coroutines.CoroutineContext

internal actual fun frameClock(): CoroutineContext = AndroidUiDispatcher.Main
