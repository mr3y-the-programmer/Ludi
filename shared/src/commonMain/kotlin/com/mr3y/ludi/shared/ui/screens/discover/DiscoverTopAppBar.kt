package com.mr3y.ludi.shared.ui.screens.discover

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.focused
import androidx.compose.ui.semantics.imeAction
import androidx.compose.ui.semantics.performImeAction
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DiscoverTopBar(
    searchQuery: String,
    onSearchQueryValueChanged: (String) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    val softwareKeyboard = LocalSoftwareKeyboardController.current
    TopAppBar(
        title = {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryValueChanged,
                placeholder = {
                    Text(text = strings.discover_page_search_field_placeholder)
                },
                colors = TextFieldDefaults.colors(
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                leadingIcon = {
                    IconButton(
                        onClick = { },
                        modifier = Modifier.clearAndSetSemantics { }
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Filled.Search),
                            contentDescription = null,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                },
                shape = RoundedCornerShape(50),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { softwareKeyboard?.hide() }),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxWidth()
                    .clearAndSetSemantics {
                        focused = true
                        contentDescription = strings.discover_page_search_field_content_description
                        imeAction = ImeAction.Search
                        performImeAction {
                            softwareKeyboard?.hide() ?: return@performImeAction false
                            true
                        }
                    }
            )
        },
        modifier = modifier,
        actions = {
            IconButton(
                onClick = onCloseClicked,
                modifier = Modifier.requiredSize(48.dp)
                    .semantics {
                        contentDescription = strings.discover_page_filter_icon_content_description
                    }
            ) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Filled.Tune),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        scrollBehavior = scrollBehavior
    )
}
