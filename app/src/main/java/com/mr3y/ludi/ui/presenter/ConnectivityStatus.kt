package com.mr3y.ludi.ui.presenter

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mr3y.ludi.ui.presenter.model.ConnectionState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@Composable
fun connectivityState(): State<ConnectionState> {
    val context = LocalContext.current
    return context.connectivityStateAsFlow().collectAsStateWithLifecycle(initialValue = context.currentConnectivityState)
}

fun Context.connectivityStateAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = NetworkCallback { connectionState -> trySend(connectionState) }

    connectivityManager.registerDefaultNetworkCallback(callback)

    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)

    awaitClose {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}

private fun NetworkCallback(callback: (ConnectionState) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            callback(ConnectionState.Unavailable)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                callback(ConnectionState.Available)
            } else
                callback(ConnectionState.Unavailable)
        }
    }
}

/**
 * Network utility to get current state of internet connection
 */
val Context.currentConnectivityState: ConnectionState
    get() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityState(connectivityManager)
    }


private fun getCurrentConnectivityState(connectivityManager: ConnectivityManager): ConnectionState {
    val currentDefaultNetwork = connectivityManager.activeNetwork ?: return ConnectionState.Unavailable
    val connected = connectivityManager.getNetworkCapabilities(currentDefaultNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            ?: false

    return if (connected) ConnectionState.Available else ConnectionState.Unavailable
}
