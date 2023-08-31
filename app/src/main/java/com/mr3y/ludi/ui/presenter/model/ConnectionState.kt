package com.mr3y.ludi.ui.presenter.model

enum class ConnectionState {
    /**
     * Initial [ConnectionState] when we don't know yet about the availability of the internet connectivity on device.
     */
    Undefined,
    Available,
    Unavailable
}
