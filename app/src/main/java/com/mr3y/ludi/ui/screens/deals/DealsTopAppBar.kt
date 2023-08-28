package com.mr3y.ludi.ui.screens.deals

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.focused
import androidx.compose.ui.semantics.imeAction
import androidx.compose.ui.semantics.performImeAction
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.mr3y.ludi.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchFilterBar(
    searchQuery: String,
    onSearchQueryValueChanged: (String) -> Unit,
    onFilterClicked: () -> Unit,
    showSearchBar: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            if (showSearchBar) {
                val contentDesc = stringResource(id = R.string.deals_page_search_field_content_description)
                val softwareKeyboard = LocalSoftwareKeyboardController.current
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryValueChanged,
                    shape = RoundedCornerShape(50),
                    placeholder = {
                        Text(text = stringResource(id = R.string.deals_search_filter_bar_placeholder))
                    },
                    colors = TextFieldDefaults.colors(
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.clearAndSetSemantics { }
                        ) {
                            Icon(
                                painter = rememberVectorPainter(image = Icons.Filled.Search),
                                contentDescription = null,
                                modifier = Modifier.fillMaxHeight()
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { softwareKeyboard?.hide() }),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth()
                        .clearAndSetSemantics {
                            focused = true
                            contentDescription = contentDesc
                            imeAction = ImeAction.Search
                            performImeAction {
                                softwareKeyboard?.hide() ?: return@performImeAction false
                                true
                            }
                        }
                )
            }
        },
        actions = {
            val contentDescription = stringResource(id = R.string.deals_filter_icon_content_description)
            IconButton(
                onClick = onFilterClicked,
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
                    .size(48.dp)
                    .semantics {
                        this.contentDescription = contentDescription
                    }
                    .padding(4.dp)
            ) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Filled.Tune),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        windowInsets = WindowInsets(0.dp)
    )
}
