package com.mr3y.ludi.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mr3y.ludi.ui.presenter.connectivityState
import com.mr3y.ludi.ui.presenter.model.ConnectionState
import com.mr3y.ludi.ui.preview.LudiPreview
import com.mr3y.ludi.ui.theme.LudiTheme

@Composable
fun AnimatedNoInternetBanner(modifier: Modifier = Modifier) {
    val connectionState by connectivityState()
    AnimatedVisibility(
        visible = LocalNoInternetConnectivitySimulation.current || connectionState == ConnectionState.Unavailable,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer),
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CloudOff,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "No Internet Connection!",
                color = MaterialTheme.colorScheme.onErrorContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

internal val LocalNoInternetConnectivitySimulation = staticCompositionLocalOf { false }

@LudiPreview
@Composable
fun NoInternetBannerPreview() {
    LudiTheme {
        CompositionLocalProvider(LocalNoInternetConnectivitySimulation provides true) {
            AnimatedNoInternetBanner()
        }
    }
}
