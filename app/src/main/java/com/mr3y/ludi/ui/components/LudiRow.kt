package com.mr3y.ludi.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.placeholder
import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.ui.theme.LudiTheme
import java.time.format.DateTimeFormatter

@Composable
fun LudiNewReleasePlaceholderRow(modifier: Modifier = Modifier) {
//    val overlaidColor = LocalElevationOverlay.current?.apply(MaterialTheme.colors.surface, 4.dp)
//    val isDarkTheme = isSystemInDarkTheme()

    /*Layout(
        modifier = modifier,
        content = { *//*TODO*//* }
    ) { measurables, constraints ->
        layout() {

        }
    }*/
    Column(
        modifier = modifier
            .shadow(8.dp)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colors.backgroundTintShade),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "TitleTitleTitleTitleTitleTitleTitleTitle",
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(1f)
                .placeholder(
                    true,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(4.dp),
                    highlight = PlaceholderHighlight.fade(highlightColor = MaterialTheme.colors.surface)
                )
        )
        Text(
            text = "ReleaseDateReleaseDate",
            color = MaterialTheme.colors.releaseDate,
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(1f)
                .placeholder(
                    true,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(4.dp),
                    highlight = PlaceholderHighlight.fade(highlightColor = MaterialTheme.colors.surface)
                )
        )
    }
}

@Composable
fun LudiNewReleaseRow(
    newReleaseArticle: NewReleaseArticle,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .shadow(8.dp)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colors.backgroundTintShade),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = newReleaseArticle.title.text.removePrefix("<![CDATA[ ").removeSuffix(" ]]>"),
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
                .placeholder(
                    false,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(4.dp),
                    highlight = PlaceholderHighlight.fade(highlightColor = MaterialTheme.colors.surface)
                )
        )
        Text(
            text = newReleaseArticle.releaseDate.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
            color = MaterialTheme.colors.releaseDate,
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
                .placeholder(
                    false,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(4.dp),
                    highlight = PlaceholderHighlight.fade(highlightColor = MaterialTheme.colors.surface)
                )
        )
    }
}

val Colors.backgroundTintShade: Color
    get() = if (isLight) Color(0xFFF5F5F5) else Color(0xFF2a2a2a)

val Colors.releaseDate: Color
    get() = if (isLight) Color.DarkGray.copy(alpha = 0.75f) else Color(0xFFCECECE).copy(alpha = 0.8f)

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, backgroundColor = 0xFF121212, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LudiNewReleasePlaceholderRowPreview() {
    LudiTheme {
        LudiNewReleasePlaceholderRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(120.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun LudiNewReleaseRowPreview() {
    LudiTheme {
//        LudiNewReleaseRow(
//            newReleaseArticle =
//        )
    }
}