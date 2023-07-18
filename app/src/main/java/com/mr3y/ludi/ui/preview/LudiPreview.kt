package com.mr3y.ludi.ui.preview

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers

@Preview(name = "Light & Dynamic colors disabled", locale = "en-US", device = "id:pixel_6",  uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark & Dynamic colors disabled", locale = "en-US", device = "id:pixel_6", uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light & Dynamic Colors enabled", locale = "en-US", device = "id:pixel_6", uiMode = UI_MODE_NIGHT_NO, wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@Preview(name = "Dark & Dynamic Colors enabled", locale = "en-US", device = "id:pixel_6", uiMode = UI_MODE_NIGHT_YES, wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
annotation class LudiPreview
