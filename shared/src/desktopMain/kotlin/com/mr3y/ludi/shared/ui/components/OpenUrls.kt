package com.mr3y.ludi.shared.ui.components

import java.awt.Desktop
import java.net.URI

fun openUrlInBrowser(url: String) {
    Desktop.getDesktop().browse(URI.create(url))
}